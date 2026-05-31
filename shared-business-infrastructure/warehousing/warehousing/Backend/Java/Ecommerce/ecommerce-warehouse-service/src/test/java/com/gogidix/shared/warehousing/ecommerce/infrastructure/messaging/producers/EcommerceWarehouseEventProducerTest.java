package com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers;

import com.gogidix.shared.warehousing.ecommerce.domain.events.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EcommerceWarehouseEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private EcommerceWarehouseEventProducer producer;

    @Test
    void publishStockRegistered_sendsEvent() {
        List<WarehouseStockRegisteredEvent.StockItemInfo> items = List.of(
                WarehouseStockRegisteredEvent.StockItemInfo.builder()
                        .sku("SKU-1").quantity(100).location("ZONE-A-01").build()
        );
        producer.publishStockRegistered("v-1", "wh-01", "zone-1", items);

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.stock_registered"), eq("v-1"), any(WarehouseStockRegisteredEvent.class));
    }

    @Test
    void publishFulfillmentReceived_sendsEvent() {
        producer.publishFulfillmentReceived("ful-1", "GO-001", "SO-001", "wh-01", "zone-1");

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.fulfillment_received"), eq("GO-001"), any(FulfillmentReceivedEvent.class));
    }

    @Test
    void publishFulfillmentPicking_sendsEvent() {
        producer.publishFulfillmentPicking("ful-1", "GO-001", "SO-001", "staff-1", "John");

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.fulfillment_picking"), eq("GO-001"), any(FulfillmentPickingEvent.class));
    }

    @Test
    void publishFulfillmentPacked_sendsEvent() {
        producer.publishFulfillmentPacked("ful-1", "GO-001", "SO-001", 2.5, Map.of("length", 30.0));

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.fulfillment_packed"), eq("GO-001"), any(FulfillmentPackedEvent.class));
    }

    @Test
    void publishReadyForPickup_sendsEvent() {
        ReadyForPickupEvent event = ReadyForPickupEvent.builder()
                .fulfillmentId("ful-1").orderId("GO-001").subOrderId("SO-001")
                .warehouseId("wh-01").zoneId("zone-1")
                .warehouseAddress("123 Main St").warehouseLatitude(6.5).warehouseLongitude(3.3)
                .contactName("Manager").contactPhone("+234-000").packageCount(1)
                .deliveryType("TYPE_A").build();
        producer.publishReadyForPickup(event);

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.ready_for_pickup"), eq("GO-001"), any(ReadyForPickupEvent.class));
    }

    @Test
    void publishStockLow_sendsEvent() {
        producer.publishStockLow("v-1", "wh-01", "SKU-1", 5, 10);

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.stock_low"), eq("v-1"), any(StockLowEvent.class));
    }

    @Test
    void publishStockDepleted_sendsEvent() {
        producer.publishStockDepleted("v-1", "wh-01", "SKU-1");

        verify(kafkaTemplate).send(eq("ecommerce.warehouse.stock_depleted"), eq("v-1"), any(StockDepletedEvent.class));
    }
}
