package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.CreateBenefitPlanRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanResponse;

/**
 * Use case interface for creating benefit plans
 * Part of hexagonal architecture - input port
 */
public interface CreateBenefitPlanUseCase {

    /**
     * Create a new benefit plan
     * @param request the benefit plan creation request
     * @return the created benefit plan response
     */
    BenefitPlanResponse createBenefitPlan(CreateBenefitPlanRequest request);

    /**
     * Create multiple benefit plans in batch
     * @param requests list of benefit plan creation requests
     * @return list of created benefit plan responses
     */
    java.util.List<BenefitPlanResponse> createBenefitPlans(java.util.List<CreateBenefitPlanRequest> requests);
}
