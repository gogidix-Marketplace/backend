package com.gogidix.digitalmarketing.contentmanagement.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "content_pieces")
public class ContentPiece {

    @Id
    private String id;
    private String tenantId;
    private String title;
    private String contentType;
    private String author;
    private String status = "DRAFT";
    private String approvalStatus;
    private String approvedBy;
    private Instant approvedAt;
    private Instant publishedAt;
    private int viewCount;
    private int shareCount;
    private Boolean isFeatured;

    public ContentPiece(String tenantId, String title, String contentType, String author) {
        this.tenantId = tenantId;
        this.title = title;
        this.contentType = contentType;
        this.author = author;
        this.status = "DRAFT";
        this.viewCount = 0;
        this.shareCount = 0;
    }

    public void publish() {
        this.status = "PUBLISHED";
        this.publishedAt = Instant.now();
    }

    public void approve(String approvedBy) {
        this.approvalStatus = "APPROVED";
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
    }
}