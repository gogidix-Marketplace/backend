package com.gogidix.shared.infrastructure.services.communication.eventbus.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request.CreateEventBridgeRequestDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response.EventBridgeResponseDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.mapper.EventBridgeMapper;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.out.IEventBridgeRepository;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.in.IEventBridgeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EventBridgeService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EventBridge Service Tests")
class EventBridgeServiceTest {

    @Mock
    private EventBridgeMapper mapper;

    @Mock
    private IEventBridgeRepository repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private EventBridgeService eventBridgeService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should create event bridge")
    void shouldCreateEventBridge() {
        CreateEventBridgeRequestDto dto = new CreateEventBridgeRequestDto(
                "test-bridge", "KAFKA", "RABBITMQ",
                "source-topic", "target-topic",
                null, null, null, null,
                true, null, null, 3
        );

        EventBridge entity = new EventBridge();
        entity.setId("bridge123");

        when(repository.existsByNameAndTenantId("test-bridge", TENANT_ID)).thenReturn(false);
        when(mapper.toEntity(dto, TENANT_ID)).thenReturn(entity);
        when(repository.save(any(EventBridge.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "test-bridge", "KAFKA", "RABBITMQ",
                "source-topic", "target-topic", null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        EventBridgeResponseDto response = eventBridgeService.create(dto);

        assertNotNull(response);
        assertEquals("bridge123", response.id());
        verify(repository).existsByNameAndTenantId("test-bridge", TENANT_ID);
        verify(mapper).toEntity(dto, TENANT_ID);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should throw exception when bridge name already exists")
    void shouldThrowExceptionWhenBridgeNameAlreadyExists() {
        CreateEventBridgeRequestDto dto = new CreateEventBridgeRequestDto(
                "existing-bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 3
        );

        when(repository.existsByNameAndTenantId("existing-bridge", TENANT_ID)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> eventBridgeService.create(dto));
    }

    @Test
    @DisplayName("Should find event bridge by ID")
    void shouldFindEventBridgeById() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setName("test-bridge");

        when(repository.findByIdAndTenantId("bridge123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "test-bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        EventBridgeResponseDto response = eventBridgeService.findById("bridge123");

        assertNotNull(response);
        assertEquals("bridge123", response.id());
        verify(repository).findByIdAndTenantId("bridge123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when bridge not found by ID")
    void shouldThrowExceptionWhenBridgeNotFoundById() {
        when(repository.findByIdAndTenantId("bridge123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventBridgeService.findById("bridge123"));
    }

    @Test
    @DisplayName("Should find all event bridges")
    void shouldFindAllEventBridges() {
        EventBridge entity1 = new EventBridge();
        entity1.setId("bridge1");
        EventBridge entity2 = new EventBridge();
        entity2.setId("bridge2");

        when(repository.findAllByTenantId(TENANT_ID)).thenReturn(List.of(entity1, entity2));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge1", TENANT_ID, "bridge1", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        List<EventBridgeResponseDto> response = eventBridgeService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Should find event bridges by status")
    void shouldFindEventBridgesByStatus() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setStatus("ACTIVE");

        when(repository.findByStatusAndTenantId("ACTIVE", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        List<EventBridgeResponseDto> response = eventBridgeService.findByStatus("ACTIVE");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find event bridges by source type")
    void shouldFindEventBridgesBySourceType() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setSourceType("KAFKA");

        when(repository.findBySourceTypeAndTenantId("KAFKA", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        List<EventBridgeResponseDto> response = eventBridgeService.findBySourceType("KAFKA");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find event bridges by target type")
    void shouldFindEventBridgesByTargetType() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setTargetType("RABBITMQ");

        when(repository.findByTargetTypeAndTenantId("RABBITMQ", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        List<EventBridgeResponseDto> response = eventBridgeService.findByTargetType("RABBITMQ");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find enabled event bridges")
    void shouldFindEnabledEventBridges() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setEnabled(true);

        when(repository.findEnabledByTenantId(TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "bridge", "KAFKA", "RABBITMQ",
                null, null, null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        List<EventBridgeResponseDto> response = eventBridgeService.findEnabled();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should update event bridge")
    void shouldUpdateEventBridge() {
        CreateEventBridgeRequestDto dto = new CreateEventBridgeRequestDto(
                "updated-bridge", "KAFKA", "RABBITMQ",
                "new-source-topic", "new-target-topic",
                null, null, null, null,
                true, null, null, 3
        );

        EventBridge entity = new EventBridge();
        entity.setId("bridge123");
        entity.setName("old-bridge");

        when(repository.findByIdAndTenantId("bridge123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(EventBridge.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new EventBridgeResponseDto(
                "bridge123", TENANT_ID, "updated-bridge", "KAFKA", "RABBITMQ",
                "new-source-topic", "new-target-topic", null, null, null, null,
                true, null, null, 0, 0, 0, 0, "ACTIVE",
                LocalDateTime.now(), LocalDateTime.now()
        ));

        EventBridgeResponseDto response = eventBridgeService.update("bridge123", dto);

        assertNotNull(response);
        verify(mapper).updateEntity(entity, dto);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should delete event bridge by ID")
    void shouldDeleteEventBridgeById() {
        EventBridge entity = new EventBridge();
        entity.setId("bridge123");

        when(repository.findByIdAndTenantId("bridge123", TENANT_ID)).thenReturn(Optional.of(entity));
        doNothing().when(repository).deleteByIdAndTenantId("bridge123", TENANT_ID);

        eventBridgeService.delete("bridge123");

        verify(repository).deleteByIdAndTenantId("bridge123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent bridge")
    void shouldThrowExceptionWhenDeletingNonExistentBridge() {
        when(repository.findByIdAndTenantId("bridge123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventBridgeService.delete("bridge123"));
    }

    @Test
    @DisplayName("Should get event bridge statistics")
    void shouldGetEventBridgeStatistics() {
        when(repository.countByTenantId(TENANT_ID)).thenReturn(10L);
        when(repository.findEnabledByTenantId(TENANT_ID)).thenReturn(List.of(
                new EventBridge(), new EventBridge(), new EventBridge()
        ));
        when(repository.findByStatusAndTenantId("ACTIVE", TENANT_ID)).thenReturn(List.of(
                new EventBridge(), new EventBridge()
        ));

        IEventBridgeUseCase.EventBridgeStatsDto stats = eventBridgeService.getStats();

        assertNotNull(stats);
        assertEquals(10, stats.totalCount());
        assertEquals(3, stats.enabledCount());
        assertEquals(2, stats.activeCount());
    }
}
