package com.gogidix.centralconfiguration.featureflagservice.domain.port.in;

import java.util.Map;

/**
 * Query for evaluating a feature flag.
 */
public record EvaluateFlagQuery(
        String flagKey,
        String tenantId,
        String userId,
        Map<String, Object> context
) {
    public static EvaluateFlagQuery of(String flagKey, String tenantId, String userId, Map<String, Object> context) {
        return new EvaluateFlagQuery(flagKey, tenantId, userId, context);
    }
}
