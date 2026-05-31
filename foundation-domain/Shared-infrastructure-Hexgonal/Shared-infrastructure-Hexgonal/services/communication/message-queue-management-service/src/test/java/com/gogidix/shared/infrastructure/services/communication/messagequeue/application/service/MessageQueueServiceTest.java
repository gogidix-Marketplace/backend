package com.gogidix.shared.infrastructure.services.communication.messagequeue.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.request.CreateMessageQueueRequestDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.response.MessageQueueResponseDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.exception.MessageQueueNotFoundException;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.MessageQueue;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.out.IMessageQueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MessageQueueService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MessageQueue Service Tests")
class MessageQueueServiceTest {

    @Mock
    private IMessageQueueRepository repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private MessageQueueService messageQueueService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should create message queue")
    void shouldCreateMessageQueue() {
        CreateMessageQueueRequestDto dto = new CreateMessageQueueRequestDto(
                "test-queue", "Test queue", "STANDARD",
                "us-east-1", 1024L, 86400L, 5, 30L, 0
        );

        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");
        entity.setDescription("Test queue");

        when(repository.existsByNameAndTenantId("test-queue", TENANT_ID)).thenReturn(false);
        when(repository.save(any(MessageQueue.class))).thenReturn(entity);

        MessageQueueResponseDto response = messageQueueService.create(dto);

        assertNotNull(response);
        assertEquals("queue123", response.id());
        verify(repository).existsByNameAndTenantId("test-queue", TENANT_ID);
        verify(repository).save(any(MessageQueue.class));
    }

    @Test
    @DisplayName("Should throw exception when queue name already exists")
    void shouldThrowExceptionWhenQueueNameAlreadyExists() {
        CreateMessageQueueRequestDto dto = new CreateMessageQueueRequestDto(
                "existing-queue", null, "STANDARD",
                null, null, null, null, null, null
        );

        when(repository.existsByNameAndTenantId("existing-queue", TENANT_ID)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> messageQueueService.create(dto));
    }

    @Test
    @DisplayName("Should find message queue by ID")
    void shouldFindMessageQueueById() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");

        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.of(entity));

        MessageQueueResponseDto response = messageQueueService.findById("queue123");

        assertNotNull(response);
        assertEquals("queue123", response.id());
        verify(repository).findByIdAndTenantId("queue123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when queue not found by ID")
    void shouldThrowExceptionWhenQueueNotFoundById() {
        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(MessageQueueNotFoundException.class, () -> messageQueueService.findById("queue123"));
    }

    @Test
    @DisplayName("Should find all message queues")
    void shouldFindAllMessageQueues() {
        MessageQueue entity1 = new MessageQueue(new TenantId(TENANT_ID), "queue1", "STANDARD");
        entity1.setId("queue1");
        MessageQueue entity2 = new MessageQueue(new TenantId(TENANT_ID), "queue2", "FIFO");
        entity2.setId("queue2");

        when(repository.findAllByTenantId(TENANT_ID)).thenReturn(List.of(entity1, entity2));

        List<MessageQueueResponseDto> response = messageQueueService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Should find message queues by status")
    void shouldFindMessageQueuesByStatus() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");
        entity.setStatus("ACTIVE");

        when(repository.findByTenantIdAndStatus(TENANT_ID, "ACTIVE")).thenReturn(List.of(entity));

        List<MessageQueueResponseDto> response = messageQueueService.findByStatus("ACTIVE");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find message queues by type")
    void shouldFindMessageQueuesByType() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "FIFO");
        entity.setId("queue123");

        when(repository.findByTenantIdAndType(TENANT_ID, "FIFO")).thenReturn(List.of(entity));

        List<MessageQueueResponseDto> response = messageQueueService.findByType("FIFO");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should update message queue")
    void shouldUpdateMessageQueue() {
        CreateMessageQueueRequestDto dto = new CreateMessageQueueRequestDto(
                "test-queue", "Updated description", "STANDARD",
                null, 2048L, null, 10, null, null
        );

        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");

        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(MessageQueue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageQueueResponseDto response = messageQueueService.update("queue123", dto);

        assertNotNull(response);
        assertEquals("Updated description", response.description());
        assertEquals(2048L, response.maxSize());
        assertEquals(10, response.maxReceiveCount());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should delete message queue by ID")
    void shouldDeleteMessageQueueById() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");

        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.of(entity));
        doNothing().when(repository).deleteByTenantIdAndId(TENANT_ID, "queue123");

        messageQueueService.delete("queue123");

        verify(repository).deleteByTenantIdAndId(TENANT_ID, "queue123");
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent queue")
    void shouldThrowExceptionWhenDeletingNonExistentQueue() {
        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.empty());

        assertThrows(MessageQueueNotFoundException.class, () -> messageQueueService.delete("queue123"));
    }

    @Test
    @DisplayName("Should activate message queue")
    void shouldActivateMessageQueue() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");
        entity.setStatus("INACTIVE");

        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(MessageQueue.class))).thenReturn(entity);

        messageQueueService.activate("queue123");

        assertEquals("ACTIVE", entity.getStatus());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should deactivate message queue")
    void shouldDeactivateMessageQueue() {
        MessageQueue entity = new MessageQueue(new TenantId(TENANT_ID), "test-queue", "STANDARD");
        entity.setId("queue123");
        entity.setStatus("ACTIVE");

        when(repository.findByIdAndTenantId("queue123", TENANT_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(MessageQueue.class))).thenReturn(entity);

        messageQueueService.deactivate("queue123");

        assertEquals("INACTIVE", entity.getStatus());
        verify(repository).save(entity);
    }
}
