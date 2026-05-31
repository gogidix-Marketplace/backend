package com.gogidix.universal.tracking.application.mapper;

import com.gogidix.universal.tracking.application.dto.request.CreateEventRequestDto;
import com.gogidix.universal.tracking.application.dto.request.CreateSessionRequestDto;
import com.gogidix.universal.tracking.application.dto.response.PagedResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingMetricResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import com.gogidix.universal.tracking.domain.model.TrackingMetric;
import com.gogidix.universal.tracking.domain.model.TrackingSession;
import com.gogidix.universal.tracking.domain.port.in.CreateEventCommand;
import com.gogidix.universal.tracking.domain.port.in.CreateSessionCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MapStruct mapper for tracking entities and DTOs.
 */
@Mapper(componentModel = "spring", uses = {JsonMapper.class})
public interface TrackingMapper {

    /**
     * Convert CreateEventRequestDto to CreateEventCommand
     */
    CreateEventCommand toCommand(CreateEventRequestDto dto);

    /**
     * Convert CreateSessionRequestDto to CreateSessionCommand
     */
    CreateSessionCommand toCommand(CreateSessionRequestDto dto);

    /**
     * Convert TrackingEvent entity to response DTO
     */
    @Mapping(target = "id", expression = "java(event.getId().toString())")
    TrackingEventResponseDto toResponseDto(TrackingEvent event);

    /**
     * Convert TrackingSession entity to response DTO
     */
    @Mapping(target = "id", expression = "java(session.getId().toString())")
    @Mapping(target = "startedAt", source = "startedAt")
    TrackingSessionResponseDto toResponseDto(TrackingSession session);

    /**
     * Convert TrackingMetric entity to response DTO
     */
    @Mapping(target = "id", expression = "java(metric.getId().toString())")
    TrackingMetricResponseDto toResponseDto(TrackingMetric metric);

    /**
     * Convert list of TrackingEvent entities to response DTOs
     */
    List<TrackingEventResponseDto> toEventResponseDtoList(List<TrackingEvent> events);

    /**
     * Convert list of TrackingSession entities to response DTOs
     */
    List<TrackingSessionResponseDto> toSessionResponseDtoList(List<TrackingSession> sessions);

    /**
     * Convert list of TrackingMetric entities to response DTOs
     */
    List<TrackingMetricResponseDto> toMetricResponseDtoList(List<TrackingMetric> metrics);

    /**
     * Convert Spring Data Page to PagedResponseDto
     */
    default <T> PagedResponseDto<T> toPagedResponse(Page<T> page) {
        return PagedResponseDto.of(page);
    }
}
