package com.gogidix.centralconfiguration.configserver.domain.port.in;

import lombok.Builder;

/**
 * Query for retrieving a single configuration entry.
 */
@Builder
public record GetConfigQuery(
        Long configurationId,

        String tenantId,

        String applicationName,

        String profile,

        String configKey
) {
}
