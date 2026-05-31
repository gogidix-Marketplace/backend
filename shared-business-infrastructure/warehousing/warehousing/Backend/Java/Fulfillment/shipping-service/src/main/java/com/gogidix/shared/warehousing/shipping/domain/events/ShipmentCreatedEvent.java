package com.gogidix.shared.warehousing.shipping.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentCreatedEvent {

	private String eventId;
	private String shipmentId;
	private String orderNumber;
	private String trackingNumber;
	private String carrier;
	private String serviceLevel;
	private String status;
	private String tenantId;
	private LocalDateTime timestamp;

	public static ShipmentCreatedEventBuilder builder() {
		return new ShipmentCreatedEventBuilder()
			.eventId(java.util.UUID.randomUUID().toString())
			.timestamp(LocalDateTime.now());
	}

	public static class ShipmentCreatedEventBuilder {
		private String eventId;
		private String shipmentId;
		private String orderNumber;
		private String trackingNumber;
		private String carrier;
		private String serviceLevel;
		private String status;
		private String tenantId;
		private LocalDateTime timestamp;

		public ShipmentCreatedEventBuilder eventId(String eventId) {
			this.eventId = eventId;
			return this;
		}

		public ShipmentCreatedEventBuilder shipmentId(String shipmentId) {
			this.shipmentId = shipmentId;
			return this;
		}

		public ShipmentCreatedEventBuilder orderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
			return this;
		}

		public ShipmentCreatedEventBuilder trackingNumber(String trackingNumber) {
			this.trackingNumber = trackingNumber;
			return this;
		}

		public ShipmentCreatedEventBuilder carrier(String carrier) {
			this.carrier = carrier;
			return this;
		}

		public ShipmentCreatedEventBuilder serviceLevel(String serviceLevel) {
			this.serviceLevel = serviceLevel;
			return this;
		}

		public ShipmentCreatedEventBuilder status(String status) {
			this.status = status;
			return this;
		}

		public ShipmentCreatedEventBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public ShipmentCreatedEventBuilder timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public ShipmentCreatedEvent build() {
			return new ShipmentCreatedEvent(eventId, shipmentId, orderNumber, trackingNumber, carrier, serviceLevel, status, tenantId, timestamp);
		}
	}
}
