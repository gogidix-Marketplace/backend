package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for searching benefit plans
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBenefitPlansRequest {

    private String tenantId;

    private String countryCode;

    private BenefitType benefitType;

    private BenefitStatus status;

    private String category;

    private Boolean isVoluntary;

    private Boolean isActive;

    private LocalDate effectiveOn;

    private LocalDate expiringBefore;

    private LocalDate expiringAfter;

    private String providerId;

    private List<String> planCodes;

    private String searchQuery;

    private Integer page = 0;

    private Integer size = 20;

    private String sortBy = "planName";

    private String sortDirection = "ASC";
}
