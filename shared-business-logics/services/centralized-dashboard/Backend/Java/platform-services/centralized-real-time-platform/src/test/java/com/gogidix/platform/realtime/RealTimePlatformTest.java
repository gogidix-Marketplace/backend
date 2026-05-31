package com.gogidix.platform.realtime;

import com.gogidix.platform.realtime.application.service.ChannelManagementService;
import com.gogidix.platform.realtime.application.service.MessageBroadcastService;
import com.gogidix.platform.realtime.domain.model.*;
import com.gogidix.platform.realtime.domain.port.out.ChannelRepository;
import com.gogidix.platform.realtime.domain.port.out.RealTimePublisher;
import com.gogidix.platform.realtime.interfaces.rest.ChannelController;
import com.gogidix.platform.realtime.interfaces.rest.ChannelController.CreateChannelRequest;
import com.gogidix.platform.realtime.interfaces.rest.ChannelController.SubscribeRequest;
import com.gogidix.platform.realtime.interfaces.rest.HealthController;
import com.gogidix.platform.realtime.interfaces.rest.MessageController;
import com.gogidix.platform.realtime.interfaces.rest.MessageController.BroadcastRequest;
import com.gogidix.platform.realtime.interfaces.rest.MessageController.DirectMessageRequest;
import com.gogidix.platform.realtime.interfaces.rest.MessageController.BroadcastMultipleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Centralized Real-Time Platform Tests")
class RealTimePlatformTest {

    @Nested
    @DisplayName("RealTimeChannel Tests")
    class RealTimeChannelTest {

        @Test
        @DisplayName("Should create channel with static factory")
        void shouldCreateWithFactory() {
            RealTimeChannel ch = RealTimeChannel.create("t1", "my-channel", "desc", ChannelType.PUBLIC);
            assertEquals("t1", ch.getTenantId());
            assertEquals("my-channel", ch.getName());
            assertEquals("desc", ch.getDescription());
            assertEquals(ChannelType.PUBLIC, ch.getType());
            assertEquals(ChannelStatus.ACTIVE, ch.getStatus());
            assertNotNull(ch.getCreatedAt());
            assertNotNull(ch.getUpdatedAt());
        }

        @Test
        @DisplayName("Should add subscriber")
        void shouldAddSubscriber() {
            RealTimeChannel ch = RealTimeChannel.builder()
                    .subscriberIds(new HashSet<>()).build();
            ch.addSubscriber("user1");
            assertTrue(ch.getSubscriberIds().contains("user1"));
        }

        @Test
        @DisplayName("Should remove subscriber")
        void shouldRemoveSubscriber() {
            RealTimeChannel ch = RealTimeChannel.builder()
                    .subscriberIds(new HashSet<>(Set.of("user1", "user2"))).build();
            ch.removeSubscriber("user1");
            assertFalse(ch.getSubscriberIds().contains("user1"));
            assertTrue(ch.getSubscriberIds().contains("user2"));
        }

        @Test
        @DisplayName("Should use builder")
        void shouldUseBuilder() {
            RealTimeChannel ch = RealTimeChannel.builder()
                    .id("id1").tenantId("t1").name("n").description("d")
                    .type(ChannelType.PRIVATE).status(ChannelStatus.SUSPENDED)
                    .subscriberIds(Set.of("a")).build();
            assertEquals("id1", ch.getId());
            assertEquals(ChannelType.PRIVATE, ch.getType());
            assertEquals(ChannelStatus.SUSPENDED, ch.getStatus());
        }

        @Test
        @DisplayName("Should use no-args constructor")
        void shouldUseNoArgsConstructor() {
            RealTimeChannel ch = new RealTimeChannel();
            assertNull(ch.getId());
        }
    }

    @Nested
    @DisplayName("RealTimeMessage Tests")
    class RealTimeMessageTest {

        @Test
        @DisplayName("Should create message with static factory")
        void shouldCreateWithFactory() {
            Map<String, String> meta = Map.of("key", "val");
            RealTimeMessage msg = RealTimeMessage.create("t1", "ch1", "u1", "info", "payload", meta);
            assertEquals("t1", msg.getTenantId());
            assertEquals("ch1", msg.getChannelId());
            assertEquals("u1", msg.getUserId());
            assertEquals("info", msg.getType());
            assertEquals("payload", msg.getPayload());
            assertEquals(meta, msg.getMetadata());
            assertNotNull(msg.getTimestamp());
        }

        @Test
        @DisplayName("Should use builder")
        void shouldUseBuilder() {
            RealTimeMessage msg = RealTimeMessage.builder()
                    .id("m1").tenantId("t").channelId("c").userId("u")
                    .type("alert").payload("data").build();
            assertEquals("m1", msg.getId());
            assertEquals("alert", msg.getType());
        }
    }

    @Nested
    @DisplayName("ChannelStatus Tests")
    class ChannelStatusTest {

        @Test
        @DisplayName("Should have 4 statuses with correct codes")
        void shouldHaveCorrectCodes() {
            assertEquals("active", ChannelStatus.ACTIVE.getCode());
            assertEquals("inactive", ChannelStatus.INACTIVE.getCode());
            assertEquals("suspended", ChannelStatus.SUSPENDED.getCode());
            assertEquals("closed", ChannelStatus.CLOSED.getCode());
            assertEquals(4, ChannelStatus.values().length);
        }

        @Test
        @DisplayName("Should have descriptions")
        void shouldHaveDescriptions() {
            assertNotNull(ChannelStatus.ACTIVE.getDescription());
            assertNotNull(ChannelStatus.CLOSED.getDescription());
        }
    }

    @Nested
    @DisplayName("ChannelType Tests")
    class ChannelTypeTest {

        @Test
        @DisplayName("Should have 4 types with correct codes")
        void shouldHaveCorrectCodes() {
            assertEquals("public", ChannelType.PUBLIC.getCode());
            assertEquals("private", ChannelType.PRIVATE.getCode());
            assertEquals("system", ChannelType.SYSTEM.getCode());
            assertEquals("data", ChannelType.DATA.getCode());
            assertEquals(4, ChannelType.values().length);
        }

        @Test
        @DisplayName("Should have descriptions")
        void shouldHaveDescriptions() {
            assertNotNull(ChannelType.PUBLIC.getDescription());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("ChannelManagementService Tests")
    class ChannelManagementServiceTest {

        @Mock private ChannelRepository channelRepository;
        @Mock private RealTimePublisher realTimePublisher;
        @InjectMocks private ChannelManagementService service;

        @Test
        @DisplayName("Should create channel")
        void shouldCreateChannel() {
            when(channelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            RealTimeChannel ch = service.createChannel("t1", "name", "desc", ChannelType.PUBLIC);
            assertNotNull(ch.getId());
            assertEquals("t1", ch.getTenantId());
            verify(realTimePublisher).publishEvent(eq("channel.created"), eq(ch));
        }

        @Test
        @DisplayName("Should get channel")
        void shouldGetChannel() {
            RealTimeChannel ch = RealTimeChannel.builder().id("c1").build();
            when(channelRepository.findById("c1")).thenReturn(Optional.of(ch));
            assertEquals(ch, service.getChannel("c1"));
        }

        @Test
        @DisplayName("Should throw when channel not found")
        void shouldThrowWhenNotFound() {
            when(channelRepository.findById("missing")).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> service.getChannel("missing"));
        }

        @Test
        @DisplayName("Should list channels by tenant")
        void shouldListChannels() {
            List<RealTimeChannel> list = List.of(RealTimeChannel.builder().build());
            when(channelRepository.findByTenantId("t1")).thenReturn(list);
            assertEquals(list, service.listChannels("t1"));
        }

        @Test
        @DisplayName("Should delete channel")
        void shouldDeleteChannel() {
            service.deleteChannel("c1");
            verify(channelRepository).deleteById("c1");
            verify(realTimePublisher).publishEvent("channel.deleted", "c1");
        }

        @Test
        @DisplayName("Should add subscriber")
        void shouldAddSubscriber() {
            RealTimeChannel ch = RealTimeChannel.builder()
                    .id("c1").subscriberIds(new HashSet<>()).build();
            when(channelRepository.findById("c1")).thenReturn(Optional.of(ch));
            when(channelRepository.save(any())).thenReturn(ch);
            service.addSubscriber("c1", "u1");
            assertTrue(ch.getSubscriberIds().contains("u1"));
            verify(channelRepository).save(ch);
        }

        @Test
        @DisplayName("Should remove subscriber")
        void shouldRemoveSubscriber() {
            RealTimeChannel ch = RealTimeChannel.builder()
                    .id("c1").subscriberIds(new HashSet<>(Set.of("u1"))).build();
            when(channelRepository.findById("c1")).thenReturn(Optional.of(ch));
            when(channelRepository.save(any())).thenReturn(ch);
            service.removeSubscriber("c1", "u1");
            assertFalse(ch.getSubscriberIds().contains("u1"));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("MessageBroadcastService Tests")
    class MessageBroadcastServiceTest {

        @Mock private RealTimePublisher realTimePublisher;
        @InjectMocks private MessageBroadcastService service;

        @Test
        @DisplayName("Should broadcast message")
        void shouldBroadcast() {
            RealTimeMessage msg = RealTimeMessage.builder().id("m1").build();
            service.broadcast("ch1", msg);
            verify(realTimePublisher).publish("ch1", msg);
        }

        @Test
        @DisplayName("Should send direct message")
        void shouldSendDirect() {
            RealTimeMessage msg = RealTimeMessage.builder().id("m1").build();
            service.sendDirectMessage("user1", msg);
            verify(realTimePublisher).publish("user:user1", msg);
        }

        @Test
        @DisplayName("Should broadcast to multiple channels")
        void shouldBroadcastMultiple() {
            RealTimeMessage msg = RealTimeMessage.builder().id("m1").build();
            service.broadcastToChannels(List.of("ch1", "ch2", "ch3"), msg);
            verify(realTimePublisher, times(3)).publish(anyString(), eq(msg));
        }
    }

    @Nested
    @DisplayName("HealthController Tests")
    class HealthControllerTest {

        private final HealthController controller = new HealthController();

        @Test
        @DisplayName("Should return UP health")
        void shouldReturnHealth() {
            ResponseEntity<Map<String, String>> resp = controller.health();
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals("UP", resp.getBody().get("status"));
            assertEquals("centralized-real-time-platform", resp.getBody().get("service"));
        }

        @Test
        @DisplayName("Should return READY readiness")
        void shouldReturnReadiness() {
            ResponseEntity<Map<String, String>> resp = controller.readiness();
            assertEquals("READY", resp.getBody().get("status"));
        }

        @Test
        @DisplayName("Should return ALIVE liveness")
        void shouldReturnLiveness() {
            ResponseEntity<Map<String, String>> resp = controller.liveness();
            assertEquals("ALIVE", resp.getBody().get("status"));
        }
    }

    @Nested
    @DisplayName("ChannelController DTO Tests")
    class ChannelControllerDtoTest {

        @Test
        @DisplayName("Should build CreateChannelRequest")
        void shouldBuildCreateRequest() {
            CreateChannelRequest req = new CreateChannelRequest();
            req.setTenantId("t1");
            req.setName("n");
            req.setDescription("d");
            req.setType(ChannelType.SYSTEM);
            assertEquals("t1", req.getTenantId());
            assertEquals("n", req.getName());
            assertEquals("d", req.getDescription());
            assertEquals(ChannelType.SYSTEM, req.getType());
        }

        @Test
        @DisplayName("Should build SubscribeRequest")
        void shouldBuildSubscribeRequest() {
            SubscribeRequest req = new SubscribeRequest();
            req.setUserId("u1");
            assertEquals("u1", req.getUserId());
        }
    }

    @Nested
    @DisplayName("MessageController DTO Tests")
    class MessageControllerDtoTest {

        @Test
        @DisplayName("Should build BroadcastRequest")
        void shouldBuildBroadcastRequest() {
            BroadcastRequest req = new BroadcastRequest();
            req.setTenantId("t1");
            req.setChannelId("c1");
            req.setUserId("u1");
            req.setType("alert");
            req.setPayload("data");
            req.setMetadata(Map.of("k", "v"));
            assertEquals("c1", req.getChannelId());
            assertEquals("data", req.getPayload());
        }

        @Test
        @DisplayName("Should build DirectMessageRequest")
        void shouldBuildDirectRequest() {
            DirectMessageRequest req = new DirectMessageRequest();
            req.setTargetUserId("target1");
            req.setUserId("u1");
            assertEquals("target1", req.getTargetUserId());
        }

        @Test
        @DisplayName("Should build BroadcastMultipleRequest")
        void shouldBuildBroadcastMultipleRequest() {
            BroadcastMultipleRequest req = new BroadcastMultipleRequest();
            req.setChannelIds(List.of("c1", "c2"));
            assertEquals(2, req.getChannelIds().size());
        }
    }
}
