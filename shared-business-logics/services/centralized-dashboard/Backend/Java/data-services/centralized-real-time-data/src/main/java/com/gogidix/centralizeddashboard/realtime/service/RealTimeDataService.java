package com.gogidix.centralizeddashboard.realtime.service;

import com.gogidix.centralizeddashboard.realtime.dto.RealTimeDataDto;
import com.gogidix.centralizeddashboard.realtime.entity.RealTimeDataEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RealTimeDataService {
    
    RealTimeDataEvent processEvent(RealTimeDataDto eventDto);
    
    Optional<RealTimeDataEvent> getEventById(String eventId);
    
    List<RealTimeDataEvent> getEventsBySource(String source);
    
    List<RealTimeDataEvent> getEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    List<RealTimeDataEvent> getUnprocessedEvents();
    
    void markEventAsProcessed(String eventId);
    
    long getEventCountBySource(String source, LocalDateTime since);
    
    void cleanupOldEvents(int daysToKeep);
    
    List<RealTimeDataDto> streamCurrentEvents(String source);
}