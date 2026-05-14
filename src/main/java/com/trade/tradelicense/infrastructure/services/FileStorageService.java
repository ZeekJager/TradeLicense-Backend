package com.trade.tradelicense.infrastructure.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadRoot;
    private final long maxUploadSizeBytes;

    public FileStorageService(
            @Value("${tradelicense.storage.upload-dir:uploads}") String uploadDir,
            @Value("${tradelicense.storage.max-upload-size-bytes:2097152}") long maxUploadSizeBytes
    ) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        this.maxUploadSizeBytes = maxUploadSizeBytes;
        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not create upload directory", exception);
        }
    }

    public String store(String fileName) {
        return "uploads/" + fileName;
    }

    public StoredFileDescriptor store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        if (file.getSize() > maxUploadSizeBytes) {
            throw new IllegalArgumentException("File size must not exceed 2 MB");
        }
        String originalFileName = Path.of(file.getOriginalFilename() == null ? "upload.bin" : file.getOriginalFilename())
                .getFileName()
                .toString();
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFileName.substring(dotIndex);
        }
        String storedFileName = UUID.randomUUID() + extension;
        Path target = uploadRoot.resolve(storedFileName).normalize();
        if (!target.startsWith(uploadRoot)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not store uploaded file", exception);
        }
        return new StoredFileDescriptor(
                originalFileName,
                storedFileName,
                file.getContentType() == null ? "application/octet-stream" : file.getContentType(),
                file.getSize()
        );
    }

    public Resource load(String storedFileName) {
        try {
            Path file = uploadRoot.resolve(storedFileName).normalize();
            if (!file.startsWith(uploadRoot) || !Files.exists(file)) {
                throw new IllegalArgumentException("File not found");
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("File not readable");
            }
            return resource;
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Invalid file path", exception);
        }
    }
}
