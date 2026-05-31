package com.gogidix.centralizeddashboard.realtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeDataDto {
    
    private String id;
    private String source;
    private String eventType;
    private Map<String, Object> data;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String userId;
    private String sessionId;
    private Double value;
    private String unit;
}