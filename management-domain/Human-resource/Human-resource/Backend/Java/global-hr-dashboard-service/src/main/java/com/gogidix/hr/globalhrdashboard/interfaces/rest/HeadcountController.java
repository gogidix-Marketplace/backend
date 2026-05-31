package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.HeadcountResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.HeadcountQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.CountryHeadcount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Headcount Tracking
 * Provides 12 endpoints for managing headcount data
 */
@RestController
@RequestMapping("/headcount")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Headcount", description = "API for managing workforce headcount")
public class HeadcountController {

    private final HeadcountQueryService headcountQueryService;

    @GetMapping("/global")
    @Operation(summary = "Get global headcount", description = "Returns total headcount across all regions")
    public ResponseEntity<HeadcountQueryService.GlobalHeadcount> getGlobalHeadcount(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        HeadcountQueryService.GlobalHeadcount headcount = headcountQueryService.getGlobalHeadcount(period);
        return ResponseEntity.ok(headcount);
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get headcount by country", description = "Returns headcount for a specific country")
    public ResponseEntity<HeadcountResponseDto> getHeadcountByCountry(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        CountryHeadcount headcount = headcountQueryService.getHeadcountByCountry(countryCode, period);
        return ResponseEntity.ok(toResponseDto(headcount));
    }

    @GetMapping("/by-department/{department}")
    @Operation(summary = "Get headcount by department", description = "Returns headcount for a specific department")
    public ResponseEntity<List<HeadcountResponseDto>> getHeadcountByDepartment(
            @Parameter(description = "Department name", required = true)
            @PathVariable String department,
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<CountryHeadcount> headcounts = headcountQueryService.getHeadcountByDepartment(department, period);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/trend")
    @Operation(summary = "Get headcount trend", description = "Returns headcount trend over time")
    public ResponseEntity<List<HeadcountResponseDto>> getHeadcountTrend(
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<CountryHeadcount> headcounts = headcountQueryService.getHeadcountTrend(startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/by-region/{regionCode}")
    @Operation(summary = "Get headcount by region", description = "Returns headcount for a specific region")
    public ResponseEntity<List<HeadcountResponseDto>> getHeadcountByRegion(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<CountryHeadcount> headcounts = headcountQueryService.getHeadcountByRegion(regionCode, period);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/countries")
    @Operation(summary = "Get all countries", description = "Returns list of all countries with headcount data")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countries = headcountQueryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/departments")
    @Operation(summary = "Get all departments", description = "Returns list of all departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = headcountQueryService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/country/{countryCode}/trend")
    @Operation(summary = "Get country trend", description = "Returns headcount trend for a specific country")
    public ResponseEntity<List<HeadcountResponseDto>> getCountryTrend(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<CountryHeadcount> headcounts = headcountQueryService.getHeadcountTrendByCountry(
                countryCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/department/{department}/trend")
    @Operation(summary = "Get department trend", description = "Returns headcount trend for a specific department")
    public ResponseEntity<List<HeadcountResponseDto>> getDepartmentTrend(
            @Parameter(description = "Department name", required = true)
            @PathVariable String department,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<CountryHeadcount> headcounts = headcountQueryService.getHeadcountTrendByDepartment(
                department, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/top-countries")
    @Operation(summary = "Get top countries by headcount", description = "Returns countries with highest headcount")
    public ResponseEntity<List<HeadcountResponseDto>> getTopCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "10") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<CountryHeadcount> headcounts = headcountQueryService.getTopCountriesByHeadcount(period, limit);
        return ResponseEntity.ok(toResponseDtoList(headcounts));
    }

    @GetMapping("/growth")
    @Operation(summary = "Get headcount growth", description = "Returns headcount growth metrics")
    public ResponseEntity<HeadcountQueryService.HeadcountGrowth> getHeadcountGrowth(
            @Parameter(description = "Current period", required = true)
            @RequestParam String currentPeriod,
            @Parameter(description = "Previous period to compare against")
            @RequestParam(required = false) String previousPeriod) {
        if (previousPeriod == null) {
            // Calculate previous period
            try {
                java.time.YearMonth current = java.time.YearMonth.parse(currentPeriod,
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                java.time.YearMonth previous = current.minusMonths(1);
                previousPeriod = previous.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            } catch (Exception e) {
                previousPeriod = currentPeriod;
            }
        }

        HeadcountQueryService.HeadcountGrowth growth = headcountQueryService.getHeadcountGrowth(
                currentPeriod, previousPeriod);
        return ResponseEntity.ok(growth);
    }

    @GetMapping("/department-summary")
    @Operation(summary = "Get department summary", description = "Returns headcount breakdown by department")
    public ResponseEntity<java.util.Map<String, Integer>> getDepartmentSummary(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        java.util.Map<String, Integer> summary = headcountQueryService.getDepartmentSummary(period);
        return ResponseEntity.ok(summary);
    }

    private List<HeadcountResponseDto> toResponseDtoList(List<CountryHeadcount> headcounts) {
        return headcounts.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private HeadcountResponseDto toResponseDto(CountryHeadcount headcount) {
        return HeadcountResponseDto.builder()
                .id(headcount.getId())
                .countryCode(headcount.getCountryCode())
                .countryName(headcount.getCountryName())
                .regionCode(headcount.getRegionCode())
                .totalHeadcount(headcount.getTotalHeadcount())
                .permanentEmployees(headcount.getPermanentEmployees())
                .contractors(headcount.getContractors())
                .interns(headcount.getInterns())
                .department(headcount.getDepartment())
                .period(headcount.getPeriod())
                .yoyChange(headcount.getYoyChange())
                .momChange(headcount.getMomChange())
                .qoqChange(headcount.getQoqChange())
                .femalePercentage(headcount.getFemalePercentage())
                .malePercentage(headcount.getMalePercentage())
                .otherGenderPercentage(headcount.getOtherGenderPercentage())
                .avgAge(headcount.getAvgAge())
                .avgTenureYears(headcount.getAvgTenureYears())
                .isActive(headcount.getIsActive())
                .lastUpdated(headcount.getLastUpdated())
                .createdAt(headcount.getCreatedAt())
                .updatedAt(headcount.getUpdatedAt())
                .build();
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
