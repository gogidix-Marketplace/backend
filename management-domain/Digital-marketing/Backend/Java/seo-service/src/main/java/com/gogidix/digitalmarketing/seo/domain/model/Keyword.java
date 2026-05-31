package com.gogidix.digitalmarketing.seo.domain.model;

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
@Document(collection = "seo_keywords")
public class Keyword {

    @Id
    private String id;
    private String tenantId;
    private String keyword;
    private String domain;
    private Integer volume;
    private Integer difficulty;
    private Integer ranking;
    private Integer previousRanking;
    private Instant lastChecked;

    public Keyword(String tenantId, String keyword, String domain) {
        this.tenantId = tenantId;
        this.keyword = keyword;
        this.domain = domain;
        this.lastChecked = Instant.now();
    }

    public void updateRanking(int newRanking) {
        this.previousRanking = this.ranking;
        this.ranking = newRanking;
        this.lastChecked = Instant.now();
    }
}