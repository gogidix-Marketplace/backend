package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class ReportTemplate {
    private final String templateId;
    private final String templateName;
    private final ReportType type;
    private final ReportConfiguration configuration;
    
    private ReportTemplate(Builder builder) {
        this.templateId = Objects.requireNonNull(builder.templateId);
        this.templateName = Objects.requireNonNull(builder.templateName);
        this.type = builder.type;
        this.configuration = builder.configuration;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String generateExecutiveSummary(Map<String, Object> data) {
        return "Executive Summary based on data";
    }
    
    public List<String> extractInsights(Map<String, Object> data) {
        return List.of("Key insight 1", "Key insight 2");
    }
    
    public Map<String, Double> calculateKPIs(Map<String, Object> data) {
        return Map.of("kpi1", 100.0, "kpi2", 95.0);
    }
    
    public String generateChart(Map<String, Object> chartData, ChartConfiguration config) {
        return "/tmp/chart_" + System.currentTimeMillis() + ".png";
    }
    
    public static class Builder {
        private String templateId;
        private String templateName;
        private ReportType type;
        private ReportConfiguration configuration;
        
        public Builder withTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }
        
        public Builder withTemplateName(String templateName) {
            this.templateName = templateName;
            return this;
        }
        
        public Builder withType(ReportType type) {
            this.type = type;
            return this;
        }
        
        public Builder withConfiguration(ReportConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public ReportTemplate build() {
            if (templateId == null) {
                templateId = "template_" + System.currentTimeMillis();
            }
            if (templateName == null) {
                templateName = type != null ? type.getDescription() : "Default Template";
            }
            return new ReportTemplate(this);
        }
    }
}
