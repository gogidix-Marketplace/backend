package com.gogidix.shared.infrastructure.services.security.analytics.domain.port.in;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import java.util.List;
/**
 * Input port for SecurityEvent use cases.
 */
public interface SecurityEventPort {
    SecurityEventResponseDto create(CreateSecurityEventRequestDto dto);
    SecurityEventResponseDto findById(String id);
    List<SecurityEventResponseDto> findAll();
    List<SecurityEventResponseDto> findBySeverity(String severity);
    List<SecurityEventResponseDto> findByEventType(String eventType);
    List<SecurityEventResponseDto> findRecent(String hours);
    void delete(String id);
}
