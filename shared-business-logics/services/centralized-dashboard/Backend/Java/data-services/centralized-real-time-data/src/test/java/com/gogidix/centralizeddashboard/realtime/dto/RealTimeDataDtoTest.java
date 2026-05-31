package com.gogidix.centralizeddashboard.realtime.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RealTimeDataDtoTest {

    @Test
    void noArgsConstructor_createsInstance() {
        assertNotNull(new RealTimeDataDto());
    }

    @Test
    void builder_createsDto() {
        LocalDateTime now = LocalDateTime.now();
        RealTimeDataDto dto = RealTimeDataDto.builder()
                .id("id1")
                .source("svc")
                .eventType("ORDER_CREATED")
                .data(Map.of("key", "val"))
                .timestamp(now)
                .userId("user-1")
                .sessionId("sess-1")
                .value(42.0)
                .unit("USD")
                .build();

        assertEquals("id1", dto.getId());
        assertEquals("svc", dto.getSource());
        assertEquals("ORDER_CREATED", dto.getEventType());
        assertEquals(Map.of("key", "val"), dto.getData());
        assertEquals(now, dto.getTimestamp());
        assertEquals("user-1", dto.getUserId());
        assertEquals(42.0, dto.getValue());
    }

    @Test
    void allArgsConstructor_works() {
        RealTimeDataDto dto = new RealTimeDataDto("id", "src", "type", Map.of(), LocalDateTime.now(), "uid", "sid", 1.0, "u");
        assertEquals("id", dto.getId());
    }

    @Test
    void setters_work() {
        RealTimeDataDto dto = new RealTimeDataDto();
        dto.setId("new-id");
        dto.setSource("new-src");
        dto.setEventType("TYPE");
        dto.setData(Map.of("k", "v"));
        dto.setTimestamp(LocalDateTime.now());
        dto.setUserId("uid");
        dto.setSessionId("sid");
        dto.setValue(10.0);
        dto.setUnit("EUR");
        assertEquals("new-id", dto.getId());
        assertEquals(10.0, dto.getValue());
    }
}
