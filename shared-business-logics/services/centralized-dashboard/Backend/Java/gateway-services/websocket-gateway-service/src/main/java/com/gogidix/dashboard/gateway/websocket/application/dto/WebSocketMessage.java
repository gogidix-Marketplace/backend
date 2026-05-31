package com.gogidix.dashboard.gateway.websocket.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * WebSocket message DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketMessage {

    private String type;
    private String topic;
    private String tenantId;
    private Object data;
    private LocalDateTime timestamp;
    private String correlationId;
    private String source;
}
