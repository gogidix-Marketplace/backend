package com.gogidix.hr.benefitsadministration.domain.port.in;

import com.gogidix.hr.benefitsadministration.application.dto.request.UpdateBenefitPlanRequest;
import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanResponse;

/**
 * Use case interface for updating benefit plans
 * Part of hexagonal architecture - input port
 */
public interface UpdateBenefitPlanUseCase {

    /**
     * Update an existing benefit plan
     * @param planId the plan ID to update
     * @param request the update request
     * @return the updated benefit plan response
     */
    BenefitPlanResponse updateBenefitPlan(String planId, UpdateBenefitPlanRequest request);

    /**
     * Activate a benefit plan
     * @param planId the plan ID to activate
     * @return the updated benefit plan response
     */
    BenefitPlanResponse activateBenefitPlan(String planId);

    /**
     * Deactivate a benefit plan
     * @param planId the plan ID to deactivate
     * @return the updated benefit plan response
     */
    BenefitPlanResponse deactivateBenefitPlan(String planId);
}
