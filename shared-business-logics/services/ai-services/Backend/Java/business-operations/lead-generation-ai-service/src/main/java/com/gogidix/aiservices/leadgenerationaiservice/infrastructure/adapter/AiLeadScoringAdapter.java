package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadScore;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadTier;
import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.LeadEnrichmentPort;
import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.LeadScoringEnginePort;
import com.gogidix.aiservices.leadgenerationaiservice.infrastructure.config.AiServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiLeadScoringAdapter implements LeadScoringEnginePort, LeadEnrichmentPort {

    private final RestTemplate restTemplate;
    private final AiServiceProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public LeadScore calculateScore(Lead lead) {
        try {
            String url = properties.getBaseUrl() + properties.getScoringEndpoint();

            Map<String, Object> request = buildScoreRequest(lead);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null && response.containsKey("score")) {
                double score = ((Number) response.get("score")).doubleValue();
                String tierStr = (String) response.get("tier");
                LeadTier tier = tierStr != null ? LeadTier.fromString(tierStr) : LeadTier.fromScore(score);
                String reason = (String) response.get("reason");

                return LeadScore.builder()
                        .leadId(lead.getLeadId().toString())
                        .score(score)
                        .tier(tier)
                        .reason(reason)
                        .build();
            }

        } catch (RestClientException e) {
            log.error("Failed to get score from AI service: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid tier from AI service, using default: {}", e.getMessage());
        }

        // Fallback to basic scoring
        return LeadScore.builder()
                .leadId(lead.getLeadId().toString())
                .score(calculateFallbackScore(lead))
                .build();
    }

    @Override
    public List<LeadScore> calculateScores(List<Lead> leads) {
        return leads.stream()
                .map(this::calculateScore)
                .collect(Collectors.toList());
    }

    @Override
    public Lead enrichLead(Lead lead) {
        try {
            String url = properties.getBaseUrl() + properties.getEnrichmentEndpoint();

            Map<String, Object> request = Map.of(
                    "email", lead.getContactInfo().getEmail(),
                    "domain", extractDomain(lead.getContactInfo().getEmail())
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null && !response.isEmpty()) {
                // Update lead with enriched data
                log.info("Enriched lead with data: {}", response.keySet());
            }

        } catch (RestClientException e) {
            log.error("Failed to enrich lead from AI service: {}", e.getMessage());
        }

        return lead;
    }

    @Override
    public LeadEnrichmentPort.EnrichmentData fetchEnrichmentData(String email, String domain) {
        try {
            String url = properties.getBaseUrl() + properties.getEnrichmentEndpoint();

            Map<String, Object> request = Map.of("email", email, "domain", domain);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null) {
                return new LeadEnrichmentPort.EnrichmentData(
                        (String) response.get("company"),
                        (String) response.get("industry"),
                        (Integer) response.get("employeeCount"),
                        (Long) response.get("revenue"),
                        (String) response.get("website"),
                        (String) response.get("linkedInUrl"),
                        (String) response.get("phone"),
                        (List<String>) response.get("technologies")
                );
            }

        } catch (RestClientException e) {
            log.error("Failed to fetch enrichment data: {}", e.getMessage());
        }

        return null;
    }

    public LeadScoringEnginePort.ConversionPrediction predictConversion(Lead lead) {
        try {
            String url = properties.getBaseUrl() + "/api/predict-conversion";

            Map<String, Object> request = buildScoreRequest(lead);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null) {
                double probability = ((Number) response.getOrDefault("conversionProbability", 0.5)).doubleValue();
                String expectedDate = (String) response.get("expectedConversionDate");
                double confidence = ((Number) response.getOrDefault("confidence", 0.5)).doubleValue();

                return new LeadScoringEnginePort.ConversionPrediction(probability, expectedDate, confidence);
            }

        } catch (RestClientException e) {
            log.error("Failed to predict conversion: {}", e.getMessage());
        }

        return new LeadScoringEnginePort.ConversionPrediction(0.5, null, 0.5);
    }

    public Map<String, Object> predictOptimalContactTime(Lead lead) {
        try {
            String url = properties.getBaseUrl() + "/api/optimal-contact-time";

            Map<String, Object> response = restTemplate.postForObject(url, Map.of(), Map.class);

            if (response != null) {
                return response;
            }

        } catch (RestClientException e) {
            log.error("Failed to predict optimal contact time: {}", e.getMessage());
        }

        return Map.of("optimalTime", "10:00", "optimalDay", "Tuesday", "timezone", "UTC");
    }

    private Map<String, Object> buildScoreRequest(Lead lead) {
        Map<String, Object> request = new HashMap<>();
        request.put("leadId", lead.getLeadId().toString());
        request.put("email", lead.getContactInfo().getEmail());
        request.put("company", lead.getContactInfo().getCompany());
        request.put("jobTitle", lead.getContactInfo().getJobTitle());

        if (lead.getSource() != null) {
            request.put("source", lead.getSource().getName());
            request.put("channel", lead.getSource().getChannel().toString());
        }

        request.put("qualificationCriteria", lead.getQualificationCriteria().size());
        request.put("activityCount", lead.getActivities().size());

        return request;
    }

    private double calculateFallbackScore(Lead lead) {
        double score = 50.0;

        if (lead.getSource() != null) {
            score += lead.getSource().getQualityScore() * 0.3;
        }

        score += lead.getQualificationCriteria().size() * 5.0;
        score += Math.min(20.0, lead.getEngagementCount() * 2.0);

        return Math.min(100, score);
    }

    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf("@") + 1);
    }
}
