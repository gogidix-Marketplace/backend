package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.event.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.LeadScoringEnginePort;
import com.gogidix.aiservices.leadgenerationaiservice.infrastructure.config.AiServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("AiLeadScoringAdapter Infrastructure Tests")
class AiLeadScoringAdapterTest {

    private RestTemplate restTemplate;
    private AiServiceProperties properties;
    private ObjectMapper objectMapper;
    private AiLeadScoringAdapter adapter;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        properties = new AiServiceProperties();
        properties.setBaseUrl("http://ai-service:8080");
        properties.setScoringEndpoint("/api/score");
        properties.setEnrichmentEndpoint("/api/enrich");

        objectMapper = new ObjectMapper();
        adapter = new AiLeadScoringAdapter(restTemplate, properties, objectMapper);
    }

    @Nested
    @DisplayName("Lead Scoring Tests")
    class ScoringTests {

        @Test
        @DisplayName("Should calculate lead score from AI service")
        void shouldCalculateLeadScore() {
            Lead lead = createTestLead();
            Map<String, Object> response = new HashMap<>();
            response.put("score", 75.0);
            response.put("tier", "HIGH_QUALITY");
            response.put("confidence", 0.85);

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(response);

            LeadScore score = adapter.calculateScore(lead);

            assertThat(score).isNotNull();
            assertThat(score.getScore()).isEqualTo(75.0);
            assertThat(score.getTier()).isEqualTo(LeadTier.HIGH_QUALITY);
        }

        @Test
        @DisplayName("Should handle null response gracefully")
        void shouldHandleNullResponse() {
            Lead lead = createTestLead();

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(null);

            LeadScore score = adapter.calculateScore(lead);

            assertThat(score).isNotNull();
            assertThat(score.getScore()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should handle RestClientException")
        void shouldHandleRestClientException() {
            Lead lead = createTestLead();

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            LeadScore score = adapter.calculateScore(lead);

            // Should fall back to default scoring
            assertThat(score).isNotNull();
        }

        @Test
        @DisplayName("Should include lead attributes in request")
        void shouldIncludeLeadAttributes() {
            Lead lead = createTestLead();
            lead.setSource(LeadSource.builder()
                    .name("Referral")
                    .channel(LeadChannel.REFERRAL)
                    .build());

            Map<String, Object> response = new HashMap<>();
            response.put("score", 85.0);
            response.put("tier", "HIGH_QUALITY");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(response);

            adapter.calculateScore(lead);

            verify(restTemplate).postForObject(any(String.class), any(), eq(Map.class));
        }
    }

    @Nested
    @DisplayName("Lead Enrichment Tests")
    class EnrichmentTests {

        @Test
        @DisplayName("Should enrich lead data from AI service")
        void shouldEnrichLeadData() {
            Lead lead = createTestLead();

            Map<String, Object> enrichmentData = new HashMap<>();
            enrichmentData.put("company", "Enriched Corp");
            enrichmentData.put("industry", "Technology");
            enrichmentData.put("employeeCount", 500);
            enrichmentData.put("revenue", 50000000L);

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(enrichmentData);

            Lead enriched = adapter.enrichLead(lead);

            assertThat(enriched).isNotNull();
        }

        @Test
        @DisplayName("Should handle missing enrichment data")
        void shouldHandleMissingEnrichmentData() {
            Lead lead = createTestLead();

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(null);

            Lead enriched = adapter.enrichLead(lead);

            assertThat(enriched).isNotNull();
            // Should return original lead when enrichment fails
        }

        @Test
        @DisplayName("Should add enriched data to lead")
        void shouldAddEnrichedDataToLead() {
            Lead lead = createTestLead();

            Map<String, Object> enrichmentData = new HashMap<>();
            enrichmentData.put("company", "Acme Corporation");
            enrichmentData.put("website", "https://acme.com");
            enrichmentData.put("linkedin", "https://linkedin.com/company/acme");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(enrichmentData);

            Lead enriched = adapter.enrichLead(lead);

            assertThat(enriched).isNotNull();
        }
    }

    @Nested
    @DisplayName("Batch Scoring Tests")
    class BatchScoringTests {

        @Test
        @DisplayName("Should score multiple leads")
        void shouldScoreMultipleLeads() {
            List<Lead> leads = List.of(createTestLead(), createTestLead());

            Map<String, Object> response = new HashMap<>();
            response.put("scores", List.of(
                    Map.of("leadId", leads.get(0).getLeadId().toString(), "score", 70.0),
                    Map.of("leadId", leads.get(1).getLeadId().toString(), "score", 80.0)
            ));

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(response);

            List<LeadScore> scores = adapter.calculateScores(leads);

            assertThat(scores).hasSize(2);
        }

        @Test
        @DisplayName("Should handle empty lead list")
        void shouldHandleEmptyLeadList() {
            List<LeadScore> scores = adapter.calculateScores(List.of());

            assertThat(scores).isEmpty();
        }
    }

    @Nested
    @DisplayName("Prediction Tests")
    class PredictionTests {

        @Test
        @DisplayName("Should predict lead conversion probability")
        void shouldPredictConversionProbability() {
            Lead lead = createTestLead();

            Map<String, Object> response = new HashMap<>();
            response.put("conversionProbability", 0.75);
            response.put("expectedConversionDate", "2024-03-01");
            response.put("confidence", 0.80);

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(response);

            LeadScoringEnginePort.ConversionPrediction prediction =
                    adapter.predictConversion(lead);

            assertThat(prediction).isNotNull();
            assertThat(prediction.probability()).isEqualTo(0.75);
        }

        @Test
        @DisplayName("Should predict optimal contact time")
        void shouldPredictOptimalContactTime() {
            Lead lead = createTestLead();

            Map<String, Object> response = new HashMap<>();
            response.put("optimalTime", "10:00");
            response.put("optimalDay", "Tuesday");
            response.put("timezone", "America/New_York");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenReturn(response);

            Map<String, Object> prediction = adapter.predictOptimalContactTime(lead);

            assertThat(prediction).isNotNull();
            assertThat(prediction).containsKey("optimalTime");
        }
    }

    // Helper methods

    private Lead createTestLead() {
        ContactInfo contact = ContactInfo.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .company("Acme Corp")
                .build();

        return Lead.create(contact);
    }
}
