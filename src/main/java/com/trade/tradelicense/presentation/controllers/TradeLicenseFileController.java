package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.AttachTradeLicenseApplicationDocumentCommand;
import com.trade.tradelicense.application.commands.SettleTradeLicenseApplicationPaymentCommand;
import com.trade.tradelicense.application.commands.handlers.AttachTradeLicenseApplicationDocumentHandler;
import com.trade.tradelicense.application.commands.handlers.SettleTradeLicenseApplicationPaymentHandler;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.infrastructure.persistence.JpaStoredFileEntity;
import com.trade.tradelicense.infrastructure.persistence.StoredFileKind;
import com.trade.tradelicense.infrastructure.repositories.SpringDataStoredFileRepository;
import com.trade.tradelicense.infrastructure.services.FileStorageService;
import com.trade.tradelicense.infrastructure.services.ApplicationAuditService;
import com.trade.tradelicense.presentation.dto.FileMetadataResponse;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade-license-applications/{applicationId}")
public class TradeLicenseFileController {
    private final FileStorageService fileStorageService;
    private final SpringDataStoredFileRepository storedFileRepository;
    private final AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler;
    private final SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler;
    private final ApplicationAuditService auditService;

    public TradeLicenseFileController(
            FileStorageService fileStorageService,
            SpringDataStoredFileRepository storedFileRepository,
            AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler,
            SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler,
            ApplicationAuditService auditService
    ) {
        this.fileStorageService = fileStorageService;
        this.storedFileRepository = storedFileRepository;
        this.attachDocumentHandler = attachDocumentHandler;
        this.settlePaymentHandler = settlePaymentHandler;
        this.auditService = auditService;
    }

    @PostMapping(path = "/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TradeLicenseApplicationResponse> uploadDocument(
            @PathVariable UUID applicationId,
            @RequestParam String documentType,
            @RequestParam MultipartFile file
    ) {
        var stored = fileStorageService.store(file);
        var result = attachDocumentHandler.handle(new AttachTradeLicenseApplicationDocumentCommand(
                applicationId,
                documentType,
                stored.storedFileName()
        ));
        TradeLicenseApplication application = result.data();
        var documents = application.documentPackage().documents();
        UUID documentId = documents.get(documents.size() - 1).id().value();
        storedFileRepository.save(new JpaStoredFileEntity(
                UUID.randomUUID(),
                applicationId,
                documentId,
                StoredFileKind.DOCUMENT,
                documentType,
                stored.originalFileName(),
                stored.storedFileName(),
                stored.contentType(),
                stored.size(),
                LocalDateTime.now()
        ));
        auditService.record(applicationId, "DOCUMENT_UPLOADED", application.status(), null, null, documentType);
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(application));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<FileMetadataResponse>> listDocuments(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(storedFileRepository
                .findByApplicationIdAndKindOrderByUploadedAtDesc(applicationId, StoredFileKind.DOCUMENT)
                .stream()
                .map(FileMetadataResponse::fromEntity)
                .toList());
    }

    @GetMapping("/documents/{fileId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID applicationId, @PathVariable UUID fileId) {
        JpaStoredFileEntity file = storedFileRepository.findById(fileId)
                .filter(entity -> entity.getApplicationId().equals(applicationId))
                .filter(entity -> entity.getKind() == StoredFileKind.DOCUMENT)
                .orElseThrow(() -> new IllegalArgumentException("Document file not found"));
        return download(file);
    }

    @PostMapping(path = "/payment/slip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TradeLicenseApplicationResponse> uploadPaymentSlip(
            @PathVariable UUID applicationId,
            @RequestParam MultipartFile file
    ) {
        var stored = fileStorageService.store(file);
        storedFileRepository.save(new JpaStoredFileEntity(
                UUID.randomUUID(),
                applicationId,
                null,
                StoredFileKind.PAYMENT_SLIP,
                "BANK_SLIP",
                stored.originalFileName(),
                stored.storedFileName(),
                stored.contentType(),
                stored.size(),
                LocalDateTime.now()
        ));
        var result = settlePaymentHandler.handle(new SettleTradeLicenseApplicationPaymentCommand(applicationId));
        auditService.record(applicationId, "PAYMENT_SLIP_UPLOADED", result.data().status(), null, null, stored.originalFileName());
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @GetMapping("/payment/slip")
    public ResponseEntity<FileMetadataResponse> getPaymentSlip(@PathVariable UUID applicationId) {
        return storedFileRepository.findFirstByApplicationIdAndKindOrderByUploadedAtDesc(applicationId, StoredFileKind.PAYMENT_SLIP)
                .map(FileMetadataResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/payment/slip/download")
    public ResponseEntity<Resource> downloadPaymentSlip(@PathVariable UUID applicationId) {
        JpaStoredFileEntity file = storedFileRepository
                .findFirstByApplicationIdAndKindOrderByUploadedAtDesc(applicationId, StoredFileKind.PAYMENT_SLIP)
                .orElseThrow(() -> new IllegalArgumentException("Payment slip not found"));
        return download(file);
    }

    private ResponseEntity<Resource> download(JpaStoredFileEntity file) {
        Resource resource = fileStorageService.load(file.getStoredFileName());
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(file.getOriginalFileName())
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }
}
