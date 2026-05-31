package com.gogidix.shared.infrastructure.services.security.threat.domain.port.in;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import java.util.List;
/**
 * Input port for ThreatIndicator use cases.
 */
public interface ThreatIndicatorPort {
    ThreatIndicatorResponseDto create(CreateThreatIndicatorRequestDto dto);
    ThreatIndicatorResponseDto findById(String id);
    List<ThreatIndicatorResponseDto> findAll();
    List<ThreatIndicatorResponseDto> findActive();
    List<ThreatIndicatorResponseDto> findByType(String indicatorType);
    List<ThreatIndicatorResponseDto> findBySeverity(String severity);
    ThreatIndicatorResponseDto update(String id, UpdateThreatIndicatorRequestDto dto);
    void delete(String id);
    ThreatIndicatorResponseDto deactivate(String id);
}
