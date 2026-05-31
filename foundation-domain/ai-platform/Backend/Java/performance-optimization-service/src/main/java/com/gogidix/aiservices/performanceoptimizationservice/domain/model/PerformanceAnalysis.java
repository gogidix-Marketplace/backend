package com.gogidix.aiservices.performanceoptimizationservice.domain.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.*;
@Document(collection = "performance_analyses")
@CompoundIndex(name = "idx_perf_tenant", def = "{'tenantId': 1, 'analysisId': 1}")
public class PerformanceAnalysis {
    @Id private String id;
    @Indexed private String analysisId;
    @Indexed private String tenantId;
    private String service;
    private Instant startTime;
    private Instant endTime;
    private List<String> metrics;
    private List<Bottleneck> bottlenecks;
    private List<Recommendation> recommendations;
    private Integer score;
    private Instant createdAt;
    private PerformanceAnalysis() {
        this.bottlenecks = new ArrayList<>();
        this.recommendations = new ArrayList<>();
        this.metrics = new ArrayList<>();
    }
    public PerformanceAnalysis(String tenantId, String service, List<String> metrics) {
        this();
        this.id = UUID.randomUUID().toString();
        this.analysisId = "analysis_" + UUID.randomUUID().toString().substring(0, 8);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.service = Objects.requireNonNull(service);
        this.metrics = new ArrayList<>(Objects.requireNonNull(metrics));
        this.createdAt = Instant.now();
    }
    public void complete(List<Bottleneck> bottlenecks, List<Recommendation> recommendations, Integer score) {
        this.bottlenecks = new ArrayList<>(bottlenecks);
        this.recommendations = new ArrayList<>(recommendations);
        this.score = score;
        this.endTime = Instant.now();
    }
    public String getId() { return id; }
    public String getAnalysisId() { return analysisId; }
    public String getTenantId() { return tenantId; }
    public String getService() { return service; }
    public Instant getStartTime() { return startTime; }
    public Instant getEndTime() { return endTime; }
    public List<String> getMetrics() { return metrics; }
    public List<Bottleneck> getBottlenecks() { return bottlenecks; }
    public List<Recommendation> getRecommendations() { return recommendations; }
    public Integer getScore() { return score; }
    public Instant getCreatedAt() { return createdAt; }
    protected void setId(String id) { this.id = id; }
    protected void setAnalysisId(String analysisId) { this.analysisId = analysisId; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setService(String service) { this.service = service; }
    protected void setStartTime(Instant startTime) { this.startTime = startTime; }
    protected void setEndTime(Instant endTime) { this.endTime = endTime; }
    protected void setMetrics(List<String> metrics) { this.metrics = metrics; }
    protected void setBottlenecks(List<Bottleneck> bottlenecks) { this.bottlenecks = bottlenecks; }
    protected void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
    protected void setScore(Integer score) { this.score = score; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PerformanceAnalysis instance = new PerformanceAnalysis();
        public Builder id(String id) { instance.id = id; return this; }
        public Builder analysisId(String analysisId) { instance.analysisId = analysisId; return this; }
        public Builder tenantId(String tenantId) { instance.tenantId = tenantId; return this; }
        public Builder service(String service) { instance.service = service; return this; }
        public Builder score(Integer score) { instance.score = score; return this; }
        public Builder bottlenecks(List<Bottleneck> bottlenecks) { instance.bottlenecks = bottlenecks; return this; }
        public Builder recommendations(List<Recommendation> recommendations) { instance.recommendations = recommendations; return this; }
        public PerformanceAnalysis build() {
            if (instance.tenantId == null) throw new IllegalArgumentException("tenantId required");
            if (instance.service == null) throw new IllegalArgumentException("service required");
            if (instance.createdAt == null) instance.createdAt = Instant.now();
            return instance;
        }
    }
}
