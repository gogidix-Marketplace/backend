package com.gogidix.transaction.status.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.status.domain.entity.Subscriber;
import com.gogidix.transaction.status.domain.repository.SubscriberRepository;
import com.gogidix.transaction.status.websocket.StatusWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatusBroadcastService Tests")
class StatusBroadcastServiceTest {

    @Mock private SubscriberRepository subscriberRepository;
    @Mock private StatusWebSocketHandler webSocketHandler;
    @Mock private ObjectMapper objectMapper;
    private StatusBroadcastService service;

    @BeforeEach
    void setUp() {
        service = new StatusBroadcastService(subscriberRepository, webSocketHandler, new ObjectMapper());
    }

    @Test
    @DisplayName("Should register subscriber")
    void shouldRegisterSubscriber() {
        service.registerSubscriber("sess-1", "user-1", "conn-1");
        verify(subscriberRepository).save(any(Subscriber.class));
    }

    @Test
    @DisplayName("Should unregister subscriber")
    void shouldUnregisterSubscriber() {
        Subscriber s = Subscriber.builder().sessionId("sess-1").connectionId("conn-1").build();
        when(subscriberRepository.findByConnectionId("conn-1")).thenReturn(Optional.of(s));
        service.unregisterSubscriber("conn-1");
        verify(subscriberRepository).save(argThat(sub -> sub.getStatus() == Subscriber.SubscriptionStatus.DISCONNECTED));
    }

    @Test
    @DisplayName("Should not fail unregister when subscriber not found")
    void shouldNotFailUnregisterWhenNotFound() {
        when(subscriberRepository.findByConnectionId("conn-x")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> service.unregisterSubscriber("conn-x"));
    }

    @Test
    @DisplayName("Should subscribe to transaction")
    void shouldSubscribeToTransaction() {
        Subscriber s = Subscriber.builder().sessionId("sess-1").connectionId("conn-1").build();
        when(subscriberRepository.findByConnectionId("sess-1")).thenReturn(Optional.of(s));
        service.subscribeToTransaction("sess-1", "tx-1");
        verify(subscriberRepository).save(any(Subscriber.class));
    }

    @Test
    @DisplayName("Should unsubscribe from transaction")
    void shouldUnsubscribeFromTransaction() {
        service.subscribeToTransaction("sess-1", "tx-1");
        assertDoesNotThrow(() -> service.unsubscribeFromTransaction("sess-1", "tx-1"));
    }

    @Test
    @DisplayName("Should broadcast status update")
    void shouldBroadcastStatusUpdate() throws Exception {
        Subscriber s = Subscriber.builder().sessionId("sess-1").connectionId("conn-1").status(Subscriber.SubscriptionStatus.ACTIVE).build();
        when(subscriberRepository.findActiveSubscribersByTransactionFilter(eq("tx-1"), eq(Subscriber.SubscriptionStatus.ACTIVE)))
            .thenReturn(Collections.singletonList(s));
        assertDoesNotThrow(() -> service.broadcastStatusUpdate("tx-1", "COMPLETED", Map.of("key", "val")));
        verify(webSocketHandler).broadcastToSession(eq("sess-1"), anyString());
    }

    @Test
    @DisplayName("Should broadcast to all")
    void shouldBroadcastToAll() throws Exception {
        Subscriber s = Subscriber.builder().sessionId("sess-1").connectionId("conn-1").status(Subscriber.SubscriptionStatus.ACTIVE).build();
        when(subscriberRepository.findByStatus(Subscriber.SubscriptionStatus.ACTIVE))
            .thenReturn(Collections.singletonList(s));
        assertDoesNotThrow(() -> service.broadcastToAll("ALERT", Map.of("msg", "hello")));
        verify(webSocketHandler).broadcastToSession(eq("sess-1"), anyString());
    }

    @Test
    @DisplayName("Should get active subscriber count")
    void shouldGetActiveSubscriberCount() {
        when(subscriberRepository.countByStatus(Subscriber.SubscriptionStatus.ACTIVE)).thenReturn(5L);
        assertEquals(5L, service.getActiveSubscriberCount());
    }

    @Test
    @DisplayName("Should get active subscribers")
    void shouldGetActiveSubscribers() {
        when(subscriberRepository.findByStatus(Subscriber.SubscriptionStatus.ACTIVE)).thenReturn(Collections.emptyList());
        assertTrue(service.getActiveSubscribers().isEmpty());
    }
}
