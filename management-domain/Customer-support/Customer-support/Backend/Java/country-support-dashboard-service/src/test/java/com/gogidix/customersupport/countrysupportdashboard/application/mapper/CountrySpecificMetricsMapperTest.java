package com.gogidix.customersupport.countrysupportdashboard.application.mapper;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.CountrySpecificMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CountrySpecificMetricsMapper Tests")
class CountrySpecificMetricsMapperTest {

    private CountrySpecificMetricsMapper mapper;
    private static final String TENANT_ID = "tenant-123";

    @BeforeEach
    void setUp() {
        mapper = new CountrySpecificMetricsMapper();
    }

    @Test
    @DisplayName("toEntity should map DTO to entity correctly")
    void toEntity_ShouldMapCorrectly() {
        // Given
        CountrySpecificMetricsRequestDto.BusinessHoursDto businessHoursDto =
                CountrySpecificMetricsRequestDto.BusinessHoursDto.builder()
                        .startTime("09:00")
                        .endTime("17:00")
                        .timezone("America/New_York")
                        .workingDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                        .build();

        Map<String, Integer> ticketVolumeByChannel = new HashMap<>();
        ticketVolumeByChannel.put("Email", 50);
        ticketVolumeByChannel.put("Phone", 30);
        ticketVolumeByChannel.put("Chat", 20);

        Map<String, Integer> ticketVolumeByPriority = new HashMap<>();
        ticketVolumeByPriority.put("High", 10);
        ticketVolumeByPriority.put("Medium", 40);
        ticketVolumeByPriority.put("Low", 50);

        CountrySpecificMetricsRequestDto requestDto = CountrySpecificMetricsRequestDto.builder()
                .countryCode("US")
                .countryName("United States")
                .metricDate(LocalDate.now())
                .totalTickets(100)
                .openTickets(25)
                .resolvedTickets(70)
                .escalatedTickets(5)
                .averageResolutionTimeMinutes(120.5)
                .averageResponseTimeMinutes(15.5)
                .customerSatisfactionScore(4.2)
                .activeAgents(10)
                .ticketVolumeByChannel(ticketVolumeByChannel)
                .ticketVolumeByPriority(ticketVolumeByPriority)
                .slaComplianceRate(95.5)
                .firstContactResolutionRate(78.5)
                .region("North America")
                .language("en")
                .timezone("America/New_York")
                .businessHours(businessHoursDto)
                .build();

        // When
        CountrySpecificMetrics entity = mapper.toEntity(requestDto, TENANT_ID);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(entity.getCountryCode()).isEqualTo("US");
        assertThat(entity.getCountryName()).isEqualTo("United States");
        assertThat(entity.getMetricDate()).isEqualTo(requestDto.getMetricDate());
        assertThat(entity.getTotalTickets()).isEqualTo(100);
        assertThat(entity.getOpenTickets()).isEqualTo(25);
        assertThat(entity.getResolvedTickets()).isEqualTo(70);
        assertThat(entity.getEscalatedTickets()).isEqualTo(5);
        assertThat(entity.getAverageResolutionTimeMinutes()).isEqualTo(120.5);
        assertThat(entity.getAverageResponseTimeMinutes()).isEqualTo(15.5);
        assertThat(entity.getCustomerSatisfactionScore()).isEqualTo(4.2);
        assertThat(entity.getActiveAgents()).isEqualTo(10);
        assertThat(entity.getSlaComplianceRate()).isEqualTo(95.5);
        assertThat(entity.getFirstContactResolutionRate()).isEqualTo(78.5);
        assertThat(entity.getRegion()).isEqualTo("North America");
        assertThat(entity.getLanguage()).isEqualTo("en");
        assertThat(entity.getTimezone()).isEqualTo("America/New_York");
        assertThat(entity.getTicketVolumeByChannel()).isEqualTo(ticketVolumeByChannel);
        assertThat(entity.getTicketVolumeByPriority()).isEqualTo(ticketVolumeByPriority);
        assertThat(entity.getBusinessHours()).isNotNull();
        assertThat(entity.getBusinessHours().getStartTime()).isEqualTo("09:00");
        assertThat(entity.getBusinessHours().getEndTime()).isEqualTo("17:00");
        assertThat(entity.getBusinessHours().getTimezone()).isEqualTo("America/New_York");
        assertThat(entity.getBusinessHours().getWorkingDays()).hasSize(5);
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toEntity should handle null business hours")
    void toEntity_ShouldHandleNullBusinessHours() {
        // Given
        CountrySpecificMetricsRequestDto requestDto = CountrySpecificMetricsRequestDto.builder()
                .countryCode("UK")
                .countryName("United Kingdom")
                .metricDate(LocalDate.now())
                .totalTickets(50)
                .build();

        // When
        CountrySpecificMetrics entity = mapper.toEntity(requestDto, TENANT_ID);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getCountryCode()).isEqualTo("UK");
        assertThat(entity.getBusinessHours()).isNull();
    }

    @Test
    @DisplayName("toResponseDto should map entity to DTO correctly")
    void toResponseDto_ShouldMapCorrectly() {
        // Given
        CountrySpecificMetrics.BusinessHours businessHours = CountrySpecificMetrics.BusinessHours.builder()
                .startTime("09:00")
                .endTime("17:00")
                .timezone("America/New_York")
                .workingDays(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"))
                .build();

        Map<String, Integer> ticketVolumeByChannel = new HashMap<>();
        ticketVolumeByChannel.put("Email", 50);
        ticketVolumeByChannel.put("Phone", 30);

        CountrySpecificMetrics entity = CountrySpecificMetrics.create(
                TENANT_ID, "US", "United States", LocalDate.now()
        );
        entity.setId("metrics-123");
        entity.setTotalTickets(100);
        entity.setOpenTickets(25);
        entity.setResolvedTickets(70);
        entity.setEscalatedTickets(5);
        entity.setAverageResolutionTimeMinutes(120.5);
        entity.setAverageResponseTimeMinutes(15.5);
        entity.setCustomerSatisfactionScore(4.2);
        entity.setActiveAgents(10);
        entity.setTicketVolumeByChannel(ticketVolumeByChannel);
        entity.setSlaComplianceRate(95.5);
        entity.setFirstContactResolutionRate(78.5);
        entity.setRegion("North America");
        entity.setLanguage("en");
        entity.setTimezone("America/New_York");
        entity.setBusinessHours(businessHours);

        // When
        CountrySpecificMetricsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo("metrics-123");
        assertThat(responseDto.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(responseDto.getCountryCode()).isEqualTo("US");
        assertThat(responseDto.getCountryName()).isEqualTo("United States");
        assertThat(responseDto.getTotalTickets()).isEqualTo(100);
        assertThat(responseDto.getOpenTickets()).isEqualTo(25);
        assertThat(responseDto.getResolvedTickets()).isEqualTo(70);
        assertThat(responseDto.getEscalatedTickets()).isEqualTo(5);
        assertThat(responseDto.getAverageResolutionTimeMinutes()).isEqualTo(120.5);
        assertThat(responseDto.getAverageResponseTimeMinutes()).isEqualTo(15.5);
        assertThat(responseDto.getCustomerSatisfactionScore()).isEqualTo(4.2);
        assertThat(responseDto.getActiveAgents()).isEqualTo(10);
        assertThat(responseDto.getSlaComplianceRate()).isEqualTo(95.5);
        assertThat(responseDto.getFirstContactResolutionRate()).isEqualTo(78.5);
        assertThat(responseDto.getRegion()).isEqualTo("North America");
        assertThat(responseDto.getLanguage()).isEqualTo("en");
        assertThat(responseDto.getTimezone()).isEqualTo("America/New_York");
        assertThat(responseDto.getTicketVolumeByChannel()).isEqualTo(ticketVolumeByChannel);
        assertThat(responseDto.getBusinessHours()).isNotNull();
        assertThat(responseDto.getBusinessHours().getStartTime()).isEqualTo("09:00");
        assertThat(responseDto.getBusinessHours().getEndTime()).isEqualTo("17:00");
        assertThat(responseDto.getBusinessHours().getTimezone()).isEqualTo("America/New_York");
        assertThat(responseDto.getBusinessHours().getWorkingDays()).hasSize(5);
        assertThat(responseDto.getCreatedAt()).isNotNull();
        assertThat(responseDto.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toResponseDto should handle null business hours")
    void toResponseDto_ShouldHandleNullBusinessHours() {
        // Given
        CountrySpecificMetrics entity = CountrySpecificMetrics.create(
                TENANT_ID, "UK", "United Kingdom", LocalDate.now()
        );
        entity.setId("metrics-456");

        // When
        CountrySpecificMetricsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getBusinessHours()).isNull();
    }

    @Test
    @DisplayName("updateEntityFromDto should update entity fields")
    void updateEntityFromDto_ShouldUpdateFields() {
        // Given
        CountrySpecificMetricsRequestDto updateDto = CountrySpecificMetricsRequestDto.builder()
                .countryCode("CA")
                .countryName("Canada")
                .metricDate(LocalDate.now().plusDays(1))
                .totalTickets(200)
                .openTickets(50)
                .resolvedTickets(140)
                .escalatedTickets(10)
                .averageResolutionTimeMinutes(130.0)
                .averageResponseTimeMinutes(18.0)
                .customerSatisfactionScore(4.5)
                .activeAgents(15)
                .slaComplianceRate(97.0)
                .firstContactResolutionRate(82.0)
                .region("North America")
                .language("en")
                .timezone("America/Toronto")
                .build();

        CountrySpecificMetrics entity = CountrySpecificMetrics.create(
                TENANT_ID, "US", "United States", LocalDate.now()
        );
        entity.setId("metrics-789");
        Instant originalUpdatedAt = entity.getUpdatedAt();

        // When
        mapper.updateEntityFromDto(updateDto, entity);

        // Then
        assertThat(entity.getCountryCode()).isEqualTo("CA");
        assertThat(entity.getCountryName()).isEqualTo("Canada");
        assertThat(entity.getTotalTickets()).isEqualTo(200);
        assertThat(entity.getOpenTickets()).isEqualTo(50);
        assertThat(entity.getResolvedTickets()).isEqualTo(140);
        assertThat(entity.getEscalatedTickets()).isEqualTo(10);
        assertThat(entity.getAverageResolutionTimeMinutes()).isEqualTo(130.0);
        assertThat(entity.getAverageResponseTimeMinutes()).isEqualTo(18.0);
        assertThat(entity.getCustomerSatisfactionScore()).isEqualTo(4.5);
        assertThat(entity.getActiveAgents()).isEqualTo(15);
        assertThat(entity.getSlaComplianceRate()).isEqualTo(97.0);
        assertThat(entity.getFirstContactResolutionRate()).isEqualTo(82.0);
        assertThat(entity.getRegion()).isEqualTo("North America");
        assertThat(entity.getLanguage()).isEqualTo("en");
        assertThat(entity.getTimezone()).isEqualTo("America/Toronto");
        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("updateEntityFromDto should handle null collections")
    void updateEntityFromDto_ShouldHandleNullCollections() {
        // Given
        CountrySpecificMetricsRequestDto updateDto = CountrySpecificMetricsRequestDto.builder()
                .countryCode("FR")
                .countryName("France")
                .metricDate(LocalDate.now())
                .build();

        CountrySpecificMetrics entity = CountrySpecificMetrics.create(
                TENANT_ID, "DE", "Germany", LocalDate.now()
        );

        // When
        mapper.updateEntityFromDto(updateDto, entity);

        // Then
        assertThat(entity.getCountryCode()).isEqualTo("FR");
        assertThat(entity.getCountryName()).isEqualTo("France");
    }

    @Test
    @DisplayName("Mapper should handle round-trip conversion")
    void mapper_ShouldHandleRoundTripConversion() {
        // Given
        CountrySpecificMetricsRequestDto originalDto = CountrySpecificMetricsRequestDto.builder()
                .countryCode("JP")
                .countryName("Japan")
                .metricDate(LocalDate.now())
                .totalTickets(150)
                .openTickets(30)
                .resolvedTickets(115)
                .escalatedTickets(5)
                .averageResolutionTimeMinutes(90.0)
                .averageResponseTimeMinutes(10.0)
                .customerSatisfactionScore(4.6)
                .activeAgents(12)
                .slaComplianceRate(98.0)
                .firstContactResolutionRate(85.0)
                .region("Asia")
                .language("ja")
                .timezone("Asia/Tokyo")
                .build();

        // When
        CountrySpecificMetrics entity = mapper.toEntity(originalDto, TENANT_ID);
        CountrySpecificMetricsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto.getCountryCode()).isEqualTo(originalDto.getCountryCode());
        assertThat(responseDto.getCountryName()).isEqualTo(originalDto.getCountryName());
        assertThat(responseDto.getMetricDate()).isEqualTo(originalDto.getMetricDate());
        assertThat(responseDto.getTotalTickets()).isEqualTo(originalDto.getTotalTickets());
        assertThat(responseDto.getOpenTickets()).isEqualTo(originalDto.getOpenTickets());
        assertThat(responseDto.getResolvedTickets()).isEqualTo(originalDto.getResolvedTickets());
        assertThat(responseDto.getEscalatedTickets()).isEqualTo(originalDto.getEscalatedTickets());
        assertThat(responseDto.getAverageResolutionTimeMinutes()).isEqualTo(originalDto.getAverageResolutionTimeMinutes());
        assertThat(responseDto.getAverageResponseTimeMinutes()).isEqualTo(originalDto.getAverageResponseTimeMinutes());
        assertThat(responseDto.getCustomerSatisfactionScore()).isEqualTo(originalDto.getCustomerSatisfactionScore());
        assertThat(responseDto.getActiveAgents()).isEqualTo(originalDto.getActiveAgents());
        assertThat(responseDto.getSlaComplianceRate()).isEqualTo(originalDto.getSlaComplianceRate());
        assertThat(responseDto.getFirstContactResolutionRate()).isEqualTo(originalDto.getFirstContactResolutionRate());
        assertThat(responseDto.getRegion()).isEqualTo(originalDto.getRegion());
        assertThat(responseDto.getLanguage()).isEqualTo(originalDto.getLanguage());
        assertThat(responseDto.getTimezone()).isEqualTo(originalDto.getTimezone());
    }
}
