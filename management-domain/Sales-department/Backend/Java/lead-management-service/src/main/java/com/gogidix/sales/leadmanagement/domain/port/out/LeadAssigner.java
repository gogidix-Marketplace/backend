package com.gogidix.sales.leadmanagement.domain.port.out;

import java.util.Optional;

public interface LeadAssigner {

    enum AssignmentStrategy {
        ROUND_ROBIN, TERRITORY_BASED, WORKLOAD_BASED, MANUAL
    }

    Optional<String> assignLead(String tenantId, String territory, String segment);

    Optional<String> assignLeadWithStrategy(String tenantId, AssignmentStrategy strategy, String territory, String segment);

    AssignmentStrategy getCurrentStrategy(String tenantId);
}
