package com.gogidix.shared.warehousing.shipping.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentUpdatedEvent {

	private String eventId;
	private String shipmentId;
	private String orderNumber;
	private String trackingNumber;
	private String status;
	private String tenantId;
	private LocalDateTime timestamp;
	private String changeType;

	public static ShipmentUpdatedEventBuilder builder() {
		return new ShipmentUpdatedEventBuilder()
			.eventId(java.util.UUID.randomUUID().toString())
			.timestamp(LocalDateTime.now());
	}

	public static class ShipmentUpdatedEventBuilder {
		private String eventId;
		private String shipmentId;
		private String orderNumber;
		private String trackingNumber;
		private String status;
		private String tenantId;
		private LocalDateTime timestamp;
		private String changeType;

		public ShipmentUpdatedEventBuilder eventId(String eventId) {
			this.eventId = eventId;
			return this;
		}

		public ShipmentUpdatedEventBuilder shipmentId(String shipmentId) {
			this.shipmentId = shipmentId;
			return this;
		}

		public ShipmentUpdatedEventBuilder orderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
			return this;
		}

		public ShipmentUpdatedEventBuilder trackingNumber(String trackingNumber) {
			this.trackingNumber = trackingNumber;
			return this;
		}

		public ShipmentUpdatedEventBuilder status(String status) {
			this.status = status;
			return this;
		}

		public ShipmentUpdatedEventBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public ShipmentUpdatedEventBuilder timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public ShipmentUpdatedEventBuilder changeType(String changeType) {
			this.changeType = changeType;
			return this;
		}

		public ShipmentUpdatedEvent build() {
			return new ShipmentUpdatedEvent(eventId, shipmentId, orderNumber, trackingNumber, status, tenantId, timestamp, changeType);
		}
	}
}
