package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.statusbroadcast.application.dto.request.CreateStatusBroadcastRequestDto;
import com.gogidix.shared.infrastructure.services.communication.statusbroadcast.application.dto.response.StatusBroadcastResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Input port for Status Broadcast use cases
 */
public interface IStatusBroadcastUseCase {
    StatusBroadcastResponseDto create(CreateStatusBroadcastRequestDto dto);
    StatusBroadcastResponseDto findById(String id);
    List<StatusBroadcastResponseDto> findAll();
    List<StatusBroadcastResponseDto> findByStatus(String status);
    List<StatusBroadcastResponseDto> findByType(String broadcastType);
    List<StatusBroadcastResponseDto> findActiveBroadcasts(LocalDateTime now);
    StatusBroadcastResponseDto update(String id, CreateStatusBroadcastRequestDto dto);
    void delete(String id);
    void sendBroadcast(String id);
    void scheduleBroadcast(String id, LocalDateTime scheduledAt);
    void expireBroadcast(String id);
    long countByTenant();
}
