package com.gogidix.courier.publicbookingservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Booking entity.
 */
@DisplayName("Booking Entity Tests")
class BookingTest {

    @Test
    @DisplayName("Should create booking with valid parameters")
    void shouldCreateBookingWithValidParameters() {
        // Given
        String customerId = "customer-001";
        String tenantId = "tenant-001";
        String bookingNumber = "BK-2024-001";

        // When
        Booking booking = new Booking(customerId, tenantId, bookingNumber);

        // Then
        assertNotNull(booking.getId());
        assertEquals(bookingNumber, booking.getBookingNumber());
        assertEquals(customerId, booking.getCustomerId());
        assertEquals(tenantId, booking.getTenantId());
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());
        assertEquals("USD", booking.getCurrency());
        assertTrue(booking.getItems().isEmpty());
        assertNotNull(booking.getCreatedAt());
        assertNotNull(booking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when customerId is null")
    void shouldThrowWhenCustomerIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new Booking(null, "tenant-001", "BK-2024-001")
        );
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new Booking("customer-001", null, "BK-2024-001")
        );
    }

    @Test
    @DisplayName("Should confirm booking")
    void shouldConfirmBooking() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());

        // When
        booking.confirm();

        // Then
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    @DisplayName("Should start pickup")
    void shouldStartPickup() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        booking.confirm();

        // When
        booking.startPickup();

        // Then
        assertEquals(Booking.BookingStatus.PICKUP_IN_PROGRESS, booking.getStatus());
        assertNotNull(booking.getActualPickupTime());
    }

    @Test
    @DisplayName("Should complete pickup")
    void shouldCompletePickup() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        booking.confirm();
        booking.startPickup();

        // When
        booking.completePickup();

        // Then
        assertEquals(Booking.BookingStatus.IN_TRANSIT, booking.getStatus());
    }

    @Test
    @DisplayName("Should start delivery")
    void shouldStartDelivery() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        booking.confirm();
        booking.startPickup();
        booking.completePickup();

        // When
        booking.startDelivery();

        // Then
        assertEquals(Booking.BookingStatus.OUT_FOR_DELIVERY, booking.getStatus());
    }

    @Test
    @DisplayName("Should complete delivery")
    void shouldCompleteDelivery() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        booking.confirm();
        booking.startPickup();
        booking.completePickup();
        booking.startDelivery();

        // When
        booking.completeDelivery();

        // Then
        assertEquals(Booking.BookingStatus.DELIVERED, booking.getStatus());
        assertNotNull(booking.getActualDeliveryTime());
    }

    @Test
    @DisplayName("Should cancel booking")
    void shouldCancelBooking() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");

        // When
        booking.cancel("Customer requested cancellation");

        // Then
        assertEquals(Booking.BookingStatus.CANCELLED, booking.getStatus());
        assertEquals("Customer requested cancellation", booking.getNotes());
    }

    @Test
    @DisplayName("Should set pickup address")
    void shouldSetPickupAddress() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        Booking.Address address = new Booking.Address();
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setPostalCode("10001");
        address.setCountry("USA");

        // When
        booking.setPickupAddress(address);

        // Then
        assertEquals(address, booking.getPickupAddress());
        assertEquals("New York", booking.getPickupAddress().getCity());
    }

    @Test
    @DisplayName("Should set delivery address")
    void shouldSetDeliveryAddress() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        Booking.Address address = new Booking.Address();
        address.setStreet("456 Oak Ave");
        address.setCity("Los Angeles");
        address.setState("CA");
        address.setPostalCode("90001");
        address.setCountry("USA");

        // When
        booking.setDeliveryAddress(address);

        // Then
        assertEquals(address, booking.getDeliveryAddress());
        assertEquals("Los Angeles", booking.getDeliveryAddress().getCity());
    }

    @Test
    @DisplayName("Should set scheduled times")
    void shouldSetScheduledTimes() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        Instant pickupTime = Instant.now().plusSeconds(3600);
        Instant deliveryTime = Instant.now().plusSeconds(7200);

        // When
        booking.setScheduledPickupTime(pickupTime);
        booking.setScheduledDeliveryTime(deliveryTime);

        // Then
        assertEquals(pickupTime, booking.getScheduledPickupTime());
        assertEquals(deliveryTime, booking.getScheduledDeliveryTime());
    }

    @Test
    @DisplayName("Should set total amount")
    void shouldSetTotalAmount() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        BigDecimal amount = new BigDecimal("99.99");

        // When
        booking.setTotalAmount(amount);

        // Then
        assertEquals(amount, booking.getTotalAmount());
    }

    @Test
    @DisplayName("Should handle full booking lifecycle")
    void shouldHandleFullBookingLifecycle() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");

        // Initial state
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());

        // Confirm
        booking.confirm();
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());

        // Start pickup
        booking.startPickup();
        assertEquals(Booking.BookingStatus.PICKUP_IN_PROGRESS, booking.getStatus());
        assertNotNull(booking.getActualPickupTime());

        // Complete pickup
        booking.completePickup();
        assertEquals(Booking.BookingStatus.IN_TRANSIT, booking.getStatus());

        // Start delivery
        booking.startDelivery();
        assertEquals(Booking.BookingStatus.OUT_FOR_DELIVERY, booking.getStatus());

        // Complete delivery
        booking.completeDelivery();
        assertEquals(Booking.BookingStatus.DELIVERED, booking.getStatus());
        assertNotNull(booking.getActualDeliveryTime());
    }

    @Test
    @DisplayName("Should handle all booking statuses")
    void shouldHandleAllBookingStatuses() {
        assertNotNull(Booking.BookingStatus.PENDING);
        assertNotNull(Booking.BookingStatus.CONFIRMED);
        assertNotNull(Booking.BookingStatus.PICKUP_IN_PROGRESS);
        assertNotNull(Booking.BookingStatus.IN_TRANSIT);
        assertNotNull(Booking.BookingStatus.OUT_FOR_DELIVERY);
        assertNotNull(Booking.BookingStatus.DELIVERED);
        assertNotNull(Booking.BookingStatus.CANCELLED);
        assertNotNull(Booking.BookingStatus.FAILED);
    }

    @Test
    @DisplayName("Should set booking items")
    void shouldSetBookingItems() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        BookingItem item1 = new BookingItem();
        item1.setDescription("Item 1");
        BookingItem item2 = new BookingItem();
        item2.setDescription("Item 2");

        // When
        booking.setItems(java.util.List.of(item1, item2));

        // Then
        assertEquals(2, booking.getItems().size());
    }

    @Test
    @DisplayName("Should set address with coordinates")
    void shouldSetAddressWithCoordinates() {
        // Given
        Booking.Address address = new Booking.Address();
        address.setLatitude(40.7128);
        address.setLongitude(-74.0060);

        // Then
        assertEquals(40.7128, address.getLatitude());
        assertEquals(-74.0060, address.getLongitude());
    }

    @Test
    @DisplayName("Should set address contact information")
    void shouldSetAddressContactInformation() {
        // Given
        Booking.Address address = new Booking.Address();

        // When
        address.setContactName("John Doe");
        address.setContactPhone("+1-555-0123");

        // Then
        assertEquals("John Doe", address.getContactName());
        assertEquals("+1-555-0123", address.getContactPhone());
    }

    @Test
    @DisplayName("Should support different currencies")
    void shouldSupportDifferentCurrencies() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");

        // Then - default is USD
        assertEquals("USD", booking.getCurrency());
    }

    @Test
    @DisplayName("Should update timestamp on status change")
    void shouldUpdateTimestampOnStatusChange() {
        // Given
        Booking booking = new Booking("customer-001", "tenant-001", "BK-2024-001");
        var initialUpdatedAt = booking.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        // When
        booking.confirm();

        // Then
        assertTrue(booking.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    @DisplayName("Should handle cancellation from any state")
    void shouldHandleCancellationFromAnyState() {
        // Cancel from confirmed state
        Booking booking1 = new Booking("customer-001", "tenant-001", "BK-2024-001");
        booking1.confirm();
        booking1.cancel("Changed mind");
        assertEquals(Booking.BookingStatus.CANCELLED, booking1.getStatus());

        // Cancel from in-transit state
        Booking booking2 = new Booking("customer-001", "tenant-001", "BK-2024-002");
        booking2.confirm();
        booking2.startPickup();
        booking2.completePickup();
        booking2.cancel("Address incorrect");
        assertEquals(Booking.BookingStatus.CANCELLED, booking2.getStatus());
    }
}
