package com.gogidix.courier.publicquoteservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Quote entity.
 */
@DisplayName("Quote Entity Tests")
class QuoteTest {

    @Test
    @DisplayName("Should create quote with valid parameters")
    void shouldCreateQuoteWithValidParameters() {
        // Given
        String customerEmail = "customer@example.com";
        String serviceType = "STANDARD";

        // When
        Quote quote = new Quote(customerEmail, serviceType);

        // Then
        assertNotNull(quote.getId());
        assertNotNull(quote.getQuoteNumber());
        assertTrue(quote.getQuoteNumber().startsWith("QT-"));
        assertEquals(customerEmail, quote.getCustomerEmail());
        assertEquals(serviceType, quote.getServiceType());
        assertEquals(Quote.QuoteStatus.PENDING, quote.getStatus());
        assertEquals("USD", quote.getCurrency());
        assertTrue(quote.getItems().isEmpty());
        assertNotNull(quote.getValidUntil());
        assertNotNull(quote.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw when customerEmail is null")
    void shouldThrowWhenCustomerEmailIsNull() {
        assertThrows(NullPointerException.class, () ->
            new Quote(null, "STANDARD")
        );
    }

    @Test
    @DisplayName("Should check validity correctly")
    void shouldCheckValidityCorrectly() {
        // Given
        Quote quote = new Quote("customer@example.com", "EXPRESS");

        // Then - newly created quote should be valid
        assertTrue(quote.isValid());
        assertEquals(Quote.QuoteStatus.PENDING, quote.getStatus());
    }

    @Test
    @DisplayName("Should be invalid when not pending")
    void shouldBeInvalidWhenNotPending() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.accept();

        // When & Then
        assertFalse(quote.isValid());
    }

    @Test
    @DisplayName("Should accept quote")
    void shouldAcceptQuote() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");

        // When
        quote.accept();

        // Then
        assertEquals(Quote.QuoteStatus.ACCEPTED, quote.getStatus());
    }

    @Test
    @DisplayName("Should expire quote")
    void shouldExpireQuote() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");

        // When
        quote.expire();

        // Then
        assertEquals(Quote.QuoteStatus.EXPIRED, quote.getStatus());
    }

    @Test
    @DisplayName("Should reject quote")
    void shouldRejectQuote() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");

        // When
        quote.reject();

        // Then
        assertEquals(Quote.QuoteStatus.REJECTED, quote.getStatus());
    }

    @Test
    @DisplayName("Should calculate total with base amount only")
    void shouldCalculateTotalWithBaseAmountOnly() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("100.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should calculate total with tax")
    void shouldCalculateTotalWithTax() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        quote.setTaxAmount(new BigDecimal("10.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("110.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should calculate total with tax and surcharge")
    void shouldCalculateTotalWithTaxAndSurcharge() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        quote.setTaxAmount(new BigDecimal("10.00"));
        quote.setSurchargeAmount(new BigDecimal("5.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("115.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should calculate total with discount")
    void shouldCalculateTotalWithDiscount() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        quote.setTaxAmount(new BigDecimal("10.00"));
        quote.setDiscountAmount(new BigDecimal("5.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("105.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should calculate total with all components")
    void shouldCalculateTotalWithAllComponents() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        quote.setTaxAmount(new BigDecimal("10.00"));
        quote.setSurchargeAmount(new BigDecimal("5.00"));
        quote.setDiscountAmount(new BigDecimal("15.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("100.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should handle null components in calculation")
    void shouldHandleNullComponentsInCalculation() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        // Tax, surcharge, and discount are null

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("100.00"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should set addresses")
    void shouldSetAddresses() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");

        // When
        quote.setPickupAddress("123 Main St, New York, NY");
        quote.setDeliveryAddress("456 Oak Ave, Los Angeles, CA");

        // Then
        assertEquals("123 Main St, New York, NY", quote.getPickupAddress());
        assertEquals("456 Oak Ave, Los Angeles, CA", quote.getDeliveryAddress());
    }

    @Test
    @DisplayName("Should set estimated delivery days")
    void shouldSetEstimatedDeliveryDays() {
        // Given
        Quote quote = new Quote("customer@example.com", "EXPRESS");

        // When
        quote.setEstimatedDeliveryDays(2);

        // Then
        assertEquals(2, quote.getEstimatedDeliveryDays());
    }

    @Test
    @DisplayName("Should set quote items")
    void shouldSetQuoteItems() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        QuoteItem item1 = new QuoteItem();
        item1.setDescription("Item 1");
        QuoteItem item2 = new QuoteItem();
        item2.setDescription("Item 2");

        // When
        quote.setItems(java.util.List.of(item1, item2));

        // Then
        assertEquals(2, quote.getItems().size());
    }

    @Test
    @DisplayName("Should handle all quote statuses")
    void shouldHandleAllQuoteStatuses() {
        assertNotNull(Quote.QuoteStatus.PENDING);
        assertNotNull(Quote.QuoteStatus.ACCEPTED);
        assertNotNull(Quote.QuoteStatus.REJECTED);
        assertNotNull(Quote.QuoteStatus.EXPIRED);
    }

    @Test
    @DisplayName("Should generate unique quote numbers")
    void shouldGenerateUniqueQuoteNumbers() {
        // Given
        Quote quote1 = new Quote("customer1@example.com", "STANDARD");
        try { Thread.sleep(10); } catch (InterruptedException e) { fail("Sleep interrupted"); }
        Quote quote2 = new Quote("customer2@example.com", "EXPRESS");

        // Then
        assertNotEquals(quote1.getQuoteNumber(), quote2.getQuoteNumber());
    }

    @Test
    @DisplayName("Should support different service types")
    void shouldSupportDifferentServiceTypes() {
        // Given
        Quote standardQuote = new Quote("customer@example.com", "STANDARD");
        Quote expressQuote = new Quote("customer@example.com", "EXPRESS");
        Quote sameDayQuote = new Quote("customer@example.com", "SAME_DAY");

        // Then
        assertEquals("STANDARD", standardQuote.getServiceType());
        assertEquals("EXPRESS", expressQuote.getServiceType());
        assertEquals("SAME_DAY", sameDayQuote.getServiceType());
    }

    @Test
    @DisplayName("Should set valid until time")
    void shouldSetValidUntilTime() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        Instant newValidUntil = Instant.now().plusSeconds(3600);

        // When
        quote.setValidUntil(newValidUntil);

        // Then
        assertEquals(newValidUntil, quote.getValidUntil());
    }

    @Test
    @DisplayName("Should handle full quote lifecycle")
    void shouldHandleFullQuoteLifecycle() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("50.00"));
        quote.setTaxAmount(new BigDecimal("5.00"));

        // Initial state
        assertEquals(Quote.QuoteStatus.PENDING, quote.getStatus());
        assertTrue(quote.isValid());

        // Accept
        quote.accept();
        assertEquals(Quote.QuoteStatus.ACCEPTED, quote.getStatus());
        assertFalse(quote.isValid());
    }

    @Test
    @DisplayName("Should calculate total with precision")
    void shouldCalculateTotalWithPrecision() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("99.99"));
        quote.setTaxAmount(new BigDecimal("8.99"));
        quote.setSurchargeAmount(new BigDecimal("2.50"));
        quote.setDiscountAmount(new BigDecimal("10.00"));

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("101.48"), quote.getTotalAmount());
    }

    @Test
    @DisplayName("Should handle negative discount")
    void shouldHandleNegativeDiscount() {
        // Given
        Quote quote = new Quote("customer@example.com", "STANDARD");
        quote.setBaseAmount(new BigDecimal("100.00"));
        quote.setDiscountAmount(new BigDecimal("-5.00")); // Surcharge instead of discount

        // When
        quote.calculateTotal();

        // Then
        assertEquals(new BigDecimal("105.00"), quote.getTotalAmount());
    }
}
