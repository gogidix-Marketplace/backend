package com.gogidix.hr.benefitsadministration.infrastructure.adapter.rest;

import com.gogidix.hr.benefitsadministration.domain.port.out.EmployeeVerificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * REST adapter for employee verification
 * Connects to employee-service to validate employee information
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceAdapterImpl implements EmployeeVerificationPort {

    private final RestTemplate restTemplate;
    private static final String EMPLOYEE_SERVICE_URL = "http://employee-service/api/employees";

    @Override
    public Optional<EmployeeInfo> getEmployeeInfo(String employeeId, String tenantId) {
        try {
            String url = EMPLOYEE_SERVICE_URL + "/" + employeeId + "?tenantId=" + tenantId;
            EmployeeInfo response = restTemplate.getForObject(url, EmployeeInfo.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.warn("Failed to fetch employee info for: {}", employeeId, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean isEligibleForBenefits(String employeeId, String tenantId) {
        try {
            String url = EMPLOYEE_SERVICE_URL + "/" + employeeId + "/benefits-eligibility?tenantId=" + tenantId;
            Boolean response = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(response);
        } catch (Exception e) {
            log.warn("Failed to check eligibility for employee: {}", employeeId, e);
            return false;
        }
    }

    @Override
    public long getTenureDays(String employeeId, String tenantId) {
        try {
            String url = EMPLOYEE_SERVICE_URL + "/" + employeeId + "/tenure?tenantId=" + tenantId;
            Long response = restTemplate.getForObject(url, Long.class);
            return response != null ? response : 0;
        } catch (Exception e) {
            log.warn("Failed to fetch tenure for employee: {}", employeeId, e);
            return 0;
        }
    }

    @Override
    public boolean isEmployeeActive(String employeeId, String tenantId) {
        try {
            String url = EMPLOYEE_SERVICE_URL + "/" + employeeId + "/status?tenantId=" + tenantId;
            String response = restTemplate.getForObject(url, String.class);
            return "ACTIVE".equalsIgnoreCase(response);
        } catch (Exception e) {
            log.warn("Failed to check status for employee: {}", employeeId, e);
            return false;
        }
    }
}
