package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Country Path Gateway Filter
 *
 * <p>Extracts country code from the request path and adds it as a header
 * for downstream services.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
public class CountryPathGatewayFilter implements GatewayFilter, Ordered {

    private static final String COUNTRY_HEADER = "X-Country-Code";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Extract country from path pattern: /api/business/{country}/**
        final String countryCode = extractCountryCode(path);

        if (countryCode != null && !countryCode.isEmpty()) {
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(r -> r.headers(h -> h.set(COUNTRY_HEADER, countryCode)))
                    .build();

            log.debug("Country extracted: {} from path: {}", countryCode, path);
            return chain.filter(mutatedExchange);
        }

        return chain.filter(exchange);
    }

    private String extractCountryCode(String path) {
        String[] parts = path.split("/");
        for (int i = 0; i < parts.length; i++) {
            if ("api".equals(parts[i]) && i + 2 < parts.length && "business".equals(parts[i + 1])) {
                return parts[i + 2];
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -95;
    }
}
