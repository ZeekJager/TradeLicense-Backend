package com.trade.tradelicense.domain.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentPackage {
    private final List<ApplicationDocument> documents;

    public DocumentPackage() {
        this.documents = new ArrayList<>();
    }

    public DocumentPackage(List<ApplicationDocument> documents) {
        Objects.requireNonNull(documents, "Documents are required");
        this.documents = new ArrayList<>(documents);
    }

    public void addDocument(ApplicationDocument document) {
        documents.add(Objects.requireNonNull(document, "Document is required"));
    }

    public boolean hasDocuments() {
        return !documents.isEmpty();
    }

    public List<ApplicationDocument> documents() {
        return Collections.unmodifiableList(documents);
    }
}
