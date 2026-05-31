package com.gogidix.customersupport.countrysupportdashboard.application.mapper;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.RegionalTicketStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegionalTicketStatsMapper Tests")
class RegionalTicketStatsMapperTest {

    private RegionalTicketStatsMapper mapper;
    private static final String TENANT_ID = "tenant-123";

    @BeforeEach
    void setUp() {
        mapper = new RegionalTicketStatsMapper();
    }

    @Test
    @DisplayName("toResponseDto should map entity to DTO correctly")
    void toResponseDto_ShouldMapCorrectly() {
        // Given
        Map<String, RegionalTicketStats.CountryTicketSummary> ticketsByCountry = new HashMap<>();
        ticketsByCountry.put("US", RegionalTicketStats.CountryTicketSummary.builder()
                .countryCode("US")
                .countryName("United States")
                .totalTickets(100)
                .openTickets(25)
                .resolvedTickets(70)
                .satisfactionScore(4.2)
                .build());
        ticketsByCountry.put("CA", RegionalTicketStats.CountryTicketSummary.builder()
                .countryCode("CA")
                .countryName("Canada")
                .totalTickets(50)
                .openTickets(10)
                .resolvedTickets(38)
                .satisfactionScore(4.4)
                .build());

        Map<String, Integer> ticketsByStatus = new HashMap<>();
        ticketsByStatus.put("Open", 35);
        ticketsByStatus.put("Closed", 108);
        ticketsByStatus.put("Pending", 7);

        Map<String, Integer> ticketsByPriority = new HashMap<>();
        ticketsByPriority.put("High", 15);
        ticketsByPriority.put("Medium", 80);
        ticketsByPriority.put("Low", 55);

        Map<String, Integer> ticketsByChannel = new HashMap<>();
        ticketsByChannel.put("Email", 70);
        ticketsByChannel.put("Phone", 40);
        ticketsByChannel.put("Chat", 40);

        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "North America",
                Arrays.asList("US", "CA", "MX"),
                LocalDate.now()
        );
        entity.setId("stats-123");
        entity.setTotalTickets(150);
        entity.setNewTickets(50);
        entity.setClosedTickets(108);
        entity.setPendingTickets(7);
        entity.setTicketsByCountry(ticketsByCountry);
        entity.setTicketsByStatus(ticketsByStatus);
        entity.setTicketsByPriority(ticketsByPriority);
        entity.setTicketsByChannel(ticketsByChannel);
        entity.setAverageResolutionTimeMinutes(115.0);
        entity.setAverageResponseTimeMinutes(12.5);
        entity.setCustomerSatisfactionScore(4.3);
        entity.setSlaComplianceRate(96.5);
        entity.setChangePercentage(5.2);
        entity.setActiveAgents(20);

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo("stats-123");
        assertThat(responseDto.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(responseDto.getRegionName()).isEqualTo("North America");
        assertThat(responseDto.getCountryCodes()).containsExactly("US", "CA", "MX");
        assertThat(responseDto.getTotalTickets()).isEqualTo(150);
        assertThat(responseDto.getNewTickets()).isEqualTo(50);
        assertThat(responseDto.getClosedTickets()).isEqualTo(108);
        assertThat(responseDto.getPendingTickets()).isEqualTo(7);
        assertThat(responseDto.getTicketsByCountry()).hasSize(2);
        assertThat(responseDto.getTicketsByCountry().get("US").getCountryCode()).isEqualTo("US");
        assertThat(responseDto.getTicketsByCountry().get("US").getTotalTickets()).isEqualTo(100);
        assertThat(responseDto.getTicketsByCountry().get("CA").getCountryCode()).isEqualTo("CA");
        assertThat(responseDto.getTicketsByCountry().get("CA").getTotalTickets()).isEqualTo(50);
        assertThat(responseDto.getTicketsByStatus()).isEqualTo(ticketsByStatus);
        assertThat(responseDto.getTicketsByPriority()).isEqualTo(ticketsByPriority);
        assertThat(responseDto.getTicketsByChannel()).isEqualTo(ticketsByChannel);
        assertThat(responseDto.getAverageResolutionTimeMinutes()).isEqualTo(115.0);
        assertThat(responseDto.getAverageResponseTimeMinutes()).isEqualTo(12.5);
        assertThat(responseDto.getCustomerSatisfactionScore()).isEqualTo(4.3);
        assertThat(responseDto.getSlaComplianceRate()).isEqualTo(96.5);
        assertThat(responseDto.getChangePercentage()).isEqualTo(5.2);
        assertThat(responseDto.getActiveAgents()).isEqualTo(20);
        assertThat(responseDto.getCreatedAt()).isNotNull();
        assertThat(responseDto.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toResponseDto should handle null tickets by country")
    void toResponseDto_ShouldHandleNullTicketsByCountry() {
        // Given
        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Europe",
                Arrays.asList("UK", "DE", "FR"),
                LocalDate.now()
        );
        entity.setId("stats-456");
        entity.setTotalTickets(200);

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTicketsByCountry()).isNull();
        assertThat(responseDto.getRegionName()).isEqualTo("Europe");
        assertThat(responseDto.getTotalTickets()).isEqualTo(200);
    }

    @Test
    @DisplayName("toResponseDto should handle empty tickets by country")
    void toResponseDto_ShouldHandleEmptyTicketsByCountry() {
        // Given
        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Asia Pacific",
                Arrays.asList("JP", "AU"),
                LocalDate.now()
        );
        entity.setId("stats-789");
        entity.setTicketsByCountry(new HashMap<>());

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTicketsByCountry()).isNotNull();
        assertThat(responseDto.getTicketsByCountry()).isEmpty();
    }

    @Test
    @DisplayName("toResponseDto should handle null maps")
    void toResponseDto_ShouldHandleNullMaps() {
        // Given
        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Latin America",
                Arrays.asList("BR", "AR"),
                LocalDate.now()
        );
        entity.setId("stats-999");
        entity.setTotalTickets(75);
        entity.setTicketsByStatus(null);
        entity.setTicketsByPriority(null);
        entity.setTicketsByChannel(null);

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getTicketsByStatus()).isNull();
        assertThat(responseDto.getTicketsByPriority()).isNull();
        assertThat(responseDto.getTicketsByChannel()).isNull();
    }

    @Test
    @DisplayName("toResponseDto should map country summary correctly")
    void toResponseDto_ShouldMapCountrySummaryCorrectly() {
        // Given
        Map<String, RegionalTicketStats.CountryTicketSummary> ticketsByCountry = new HashMap<>();
        ticketsByCountry.put("DE", RegionalTicketStats.CountryTicketSummary.builder()
                .countryCode("DE")
                .countryName("Germany")
                .totalTickets(80)
                .openTickets(20)
                .resolvedTickets(58)
                .satisfactionScore(4.1)
                .build());

        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Europe",
                Arrays.asList("DE"),
                LocalDate.now()
        );
        entity.setId("stats-111");
        entity.setTicketsByCountry(ticketsByCountry);

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto.getTicketsByCountry()).hasSize(1);
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto summaryDto =
                responseDto.getTicketsByCountry().get("DE");
        assertThat(summaryDto.getCountryCode()).isEqualTo("DE");
        assertThat(summaryDto.getCountryName()).isEqualTo("Germany");
        assertThat(summaryDto.getTotalTickets()).isEqualTo(80);
        assertThat(summaryDto.getOpenTickets()).isEqualTo(20);
        assertThat(summaryDto.getResolvedTickets()).isEqualTo(58);
        assertThat(summaryDto.getSatisfactionScore()).isEqualTo(4.1);
    }

    @Test
    @DisplayName("toResponseDto should preserve all numeric values")
    void toResponseDto_ShouldPreserveNumericValues() {
        // Given
        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Test Region",
                Collections.singletonList("TS"),
                LocalDate.now()
        );
        entity.setId("stats-222");
        entity.setTotalTickets(999);
        entity.setNewTickets(111);
        entity.setClosedTickets(777);
        entity.setPendingTickets(111);
        entity.setAverageResolutionTimeMinutes(123.45);
        entity.setAverageResponseTimeMinutes(23.45);
        entity.setCustomerSatisfactionScore(3.75);
        entity.setSlaComplianceRate(87.65);
        entity.setChangePercentage(-12.34);
        entity.setActiveAgents(33);

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto.getTotalTickets()).isEqualTo(999);
        assertThat(responseDto.getNewTickets()).isEqualTo(111);
        assertThat(responseDto.getClosedTickets()).isEqualTo(777);
        assertThat(responseDto.getPendingTickets()).isEqualTo(111);
        assertThat(responseDto.getAverageResolutionTimeMinutes()).isEqualTo(123.45);
        assertThat(responseDto.getAverageResponseTimeMinutes()).isEqualTo(23.45);
        assertThat(responseDto.getCustomerSatisfactionScore()).isEqualTo(3.75);
        assertThat(responseDto.getSlaComplianceRate()).isEqualTo(87.65);
        assertThat(responseDto.getChangePercentage()).isEqualTo(-12.34);
        assertThat(responseDto.getActiveAgents()).isEqualTo(33);
    }

    @Test
    @DisplayName("toResponseDto should handle entity with minimal data")
    void toResponseDto_ShouldHandleMinimalData() {
        // Given
        RegionalTicketStats entity = RegionalTicketStats.create(
                TENANT_ID,
                "Minimal Region",
                Collections.singletonList("MR"),
                LocalDate.now()
        );
        entity.setId("stats-333");

        // When
        RegionalTicketStatsResponseDto responseDto = mapper.toResponseDto(entity);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo("stats-333");
        assertThat(responseDto.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(responseDto.getRegionName()).isEqualTo("Minimal Region");
        assertThat(responseDto.getCountryCodes()).containsExactly("MR");
        assertThat(responseDto.getStatDate()).isNotNull();
        assertThat(responseDto.getCreatedAt()).isNotNull();
        assertThat(responseDto.getUpdatedAt()).isNotNull();
    }
}
