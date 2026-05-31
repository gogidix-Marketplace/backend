package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.domain.port.in;

import com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.request.CreateRateLimitConfigRequestDto;
import com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.request.UpdateRateLimitConfigRequestDto;
import com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.response.RateLimitConfigResponseDto;

import java.util.List;

/**
 * Input port for RateLimitConfig use cases
 */
public interface RateLimitConfigPort {

    RateLimitConfigResponseDto create(CreateRateLimitConfigRequestDto dto);
    RateLimitConfigResponseDto findById(String id);
    List<RateLimitConfigResponseDto> findAll();
    List<RateLimitConfigResponseDto> findByApiKey(String apiKey);
    List<RateLimitConfigResponseDto> findActive();
    RateLimitConfigResponseDto update(String id, UpdateRateLimitConfigRequestDto dto);
    void delete(String id);
    RateLimitConfigResponseDto toggleActive(String id, boolean active);
    RateLimitConfigResponseDto updateLimits(String id, Integer requestsPerMinute, Integer requestsPerHour, Integer requestsPerDay);
}
