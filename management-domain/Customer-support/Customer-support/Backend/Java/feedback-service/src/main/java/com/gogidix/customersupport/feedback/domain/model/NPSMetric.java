package com.gogidix.customersupport.feedback.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

/**
 * NPS Metric aggregate root
 * Net Promoter Score metrics aggregation and tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "nps_metrics")
public class NPSMetric extends BaseEntity {

    @Field("metric_id")
    @Indexed(unique = true)
    private String metricId;

    @Field("period_start")
    @Indexed
    private Instant periodStart;

    @Field("period_end")
    private Instant periodEnd;

    @Field("period_type")
    private PeriodType periodType;

    @Field("nps_score")
    private Integer npsScore;

    @Field("promoters_count")
    private Integer promotersCount;

    @Field("promoters_percentage")
    private Double promotersPercentage;

    @Field("passives_count")
    private Integer passivesCount;

    @Field("passives_percentage")
    private Double passivesPercentage;

    @Field("detractors_count")
    private Integer detractorsCount;

    @Field("detractors_percentage")
    private Double detractorsPercentage;

    @Field("total_responses")
    private Integer totalResponses;

    @Field("average_score")
    private Double averageScore;

    @Field("country_code")
    @Indexed
    private String countryCode;

    @Field("agent_id")
    @Indexed
    private String agentId;

    @Field("team_id")
    @Indexed
    private String teamId;

    @Field("channel")
    private String channel;

    @Field("previous_nps_score")
    private Integer previousNpsScore;

    @Field("score_change")
    private Integer scoreChange;

    public static NPSMetric create(String tenantId, Instant periodStart, Instant periodEnd, PeriodType periodType) {
        NPSMetric metric = new NPSMetric();
        metric.setId(java.util.UUID.randomUUID().toString());
        metric.setTenantId(tenantId);
        metric.setMetricId(generateMetricId());
        metric.setPeriodStart(periodStart);
        metric.setPeriodEnd(periodEnd);
        metric.setPeriodType(periodType);
        metric.setPromotersCount(0);
        metric.setPassivesCount(0);
        metric.setDetractorsCount(0);
        metric.setTotalResponses(0);
        metric.setCreatedAt(Instant.now());
        metric.setUpdatedAt(Instant.now());
        return metric;
    }

    private static String generateMetricId() {
        return "NPS-" + System.currentTimeMillis();
    }

    public void calculateNPS() {
        if (this.totalResponses == null || this.totalResponses == 0) {
            this.npsScore = 0;
            this.promotersPercentage = 0.0;
            this.passivesPercentage = 0.0;
            this.detractorsPercentage = 0.0;
            return;
        }

        this.promotersPercentage = (this.promotersCount * 100.0) / this.totalResponses;
        this.passivesPercentage = (this.passivesCount * 100.0) / this.totalResponses;
        this.detractorsPercentage = (this.detractorsCount * 100.0) / this.totalResponses;

        this.npsScore = (int) Math.round(this.promotersPercentage - this.detractorsPercentage);
        this.updateTimestamp();
    }

    public void addResponse(Integer score) {
        this.totalResponses = (this.totalResponses != null ? this.totalResponses : 0) + 1;

        if (score >= 9) {
            this.promotersCount = (this.promotersCount != null ? this.promotersCount : 0) + 1;
        } else if (score >= 7) {
            this.passivesCount = (this.passivesCount != null ? this.passivesCount : 0) + 1;
        } else {
            this.detractorsCount = (this.detractorsCount != null ? this.detractorsCount : 0) + 1;
        }

        calculateNPS();
    }

    public enum PeriodType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
    }
}
