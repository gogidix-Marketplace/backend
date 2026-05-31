package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.in;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request.CreateEventBridgeRequestDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response.EventBridgeResponseDto;
import java.util.List;
/**
 * Input port for EventBridge use cases
 */
public interface IEventBridgeUseCase {
    EventBridgeResponseDto create(CreateEventBridgeRequestDto dto);
    EventBridgeResponseDto findById(String id);
    List<EventBridgeResponseDto> findAll();
    List<EventBridgeResponseDto> findByStatus(String status);
    List<EventBridgeResponseDto> findBySourceType(String sourceType);
    List<EventBridgeResponseDto> findByTargetType(String targetType);
    List<EventBridgeResponseDto> findEnabled();
    EventBridgeResponseDto update(String id, CreateEventBridgeRequestDto dto);
    void delete(String id);
    EventBridgeStatsDto getStats();
    record EventBridgeStatsDto(long totalCount, long enabledCount, long activeCount) {}
}
