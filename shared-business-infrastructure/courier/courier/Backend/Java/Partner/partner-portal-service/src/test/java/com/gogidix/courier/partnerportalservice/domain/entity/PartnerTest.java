package com.gogidix.courier.partnerportalservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Partner entity.
 */
@DisplayName("Partner Entity Tests")
class PartnerTest {

    @Test
    @DisplayName("Should create partner with valid parameters")
    void shouldCreatePartnerWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String partnerCode = "PARTNER-001";
        String businessName = "Acme Logistics";
        String email = "contact@acme.com";

        // When
        Partner partner = new Partner(tenantId, partnerCode, businessName, email);

        // Then
        assertNotNull(partner.getId());
        assertEquals(tenantId, partner.getTenantId());
        assertEquals(partnerCode, partner.getPartnerCode());
        assertEquals(businessName, partner.getBusinessName());
        assertEquals(email, partner.getEmail());
        assertEquals(Partner.PartnerStatus.PENDING, partner.getStatus());
        assertEquals(Partner.VerificationStatus.PENDING, partner.getVerificationStatus());
        assertEquals(0.0, partner.getRating());
        assertTrue(partner.getServiceAreas().isEmpty());
        assertTrue(partner.getCapabilities().isEmpty());
        assertNotNull(partner.getCreatedAt());
        assertNotNull(partner.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new Partner(null, "PARTNER-001", "Acme Logistics", "contact@acme.com")
        );
    }

    @Test
    @DisplayName("Should throw when partnerCode is null")
    void shouldThrowWhenPartnerCodeIsNull() {
        assertThrows(NullPointerException.class, () ->
            new Partner("tenant-001", null, "Acme Logistics", "contact@acme.com")
        );
    }

    @Test
    @DisplayName("Should create partner with null optional fields")
    void shouldCreatePartnerWithNullOptionalFields() {
        // Given
        String tenantId = "tenant-001";
        String partnerCode = "PARTNER-001";
        String businessName = "Acme Logistics";
        String email = null; // Optional

        // When
        Partner partner = new Partner(tenantId, partnerCode, businessName, email);

        // Then
        assertNotNull(partner.getId());
        assertEquals(tenantId, partner.getTenantId());
        assertEquals(partnerCode, partner.getPartnerCode());
        assertNull(partner.getEmail());
    }

    @Test
    @DisplayName("Should activate partner")
    void shouldActivatePartner() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        assertEquals(Partner.PartnerStatus.PENDING, partner.getStatus());

        // When
        partner.activate();

        // Then
        assertEquals(Partner.PartnerStatus.ACTIVE, partner.getStatus());
    }

    @Test
    @DisplayName("Should deactivate partner")
    void shouldDeactivatePartner() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        partner.activate();
        assertEquals(Partner.PartnerStatus.ACTIVE, partner.getStatus());

        // When
        partner.deactivate();

        // Then
        assertEquals(Partner.PartnerStatus.INACTIVE, partner.getStatus());
    }

    @Test
    @DisplayName("Should verify partner")
    void shouldVerifyPartner() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        assertEquals(Partner.VerificationStatus.PENDING, partner.getVerificationStatus());

        // When
        partner.verify();

        // Then
        assertEquals(Partner.VerificationStatus.VERIFIED, partner.getVerificationStatus());
    }

    @Test
    @DisplayName("Should reject verification")
    void shouldRejectVerification() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");

        // When
        partner.rejectVerification("Documents incomplete");

        // Then
        assertEquals(Partner.VerificationStatus.REJECTED, partner.getVerificationStatus());
    }

    @Test
    @DisplayName("Should update rating")
    void shouldUpdateRating() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        assertEquals(0.0, partner.getRating());

        // When
        partner.updateRating(4.5);

        // Then
        assertEquals(4.5, partner.getRating());
    }

    @Test
    @DisplayName("Should set contact name")
    void shouldSetContactName() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");

        // When
        partner.setContactName("John Doe");

        // Then
        assertEquals("John Doe", partner.getContactName());
    }

    @Test
    @DisplayName("Should set phone")
    void shouldSetPhone() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");

        // When
        partner.setPhone("+1-555-0123");

        // Then
        assertEquals("+1-555-0123", partner.getPhone());
    }

    @Test
    @DisplayName("Should set address")
    void shouldSetAddress() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        Partner.Address address = new Partner.Address();
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setPostalCode("10001");
        address.setCountry("USA");

        // When
        partner.setAddress(address);

        // Then
        assertEquals(address, partner.getAddress());
        assertEquals("New York", partner.getAddress().getCity());
    }

    @Test
    @DisplayName("Should set service areas")
    void shouldSetServiceAreas() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        java.util.List<String> areas = java.util.List.of("zone-001", "zone-002", "zone-003");

        // When
        partner.setServiceAreas(areas);

        // Then
        assertEquals(3, partner.getServiceAreas().size());
        assertTrue(partner.getServiceAreas().contains("zone-001"));
    }

    @Test
    @DisplayName("Should set capabilities")
    void shouldSetCapabilities() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        java.util.List<String> capabilities = java.util.List.of("SAME_DAY", "EXPRESS", "BULK");

        // When
        partner.setCapabilities(capabilities);

        // Then
        assertEquals(3, partner.getCapabilities().size());
        assertTrue(partner.getCapabilities().contains("SAME_DAY"));
    }

    @Test
    @DisplayName("Should handle all partner statuses")
    void shouldHandleAllPartnerStatuses() {
        assertNotNull(Partner.PartnerStatus.PENDING);
        assertNotNull(Partner.PartnerStatus.ACTIVE);
        assertNotNull(Partner.PartnerStatus.INACTIVE);
        assertNotNull(Partner.PartnerStatus.SUSPENDED);
    }

    @Test
    @DisplayName("Should handle all verification statuses")
    void shouldHandleAllVerificationStatuses() {
        assertNotNull(Partner.VerificationStatus.PENDING);
        assertNotNull(Partner.VerificationStatus.VERIFIED);
        assertNotNull(Partner.VerificationStatus.REJECTED);
        assertNotNull(Partner.VerificationStatus.IN_REVIEW);
    }

    @Test
    @DisplayName("Should handle address with coordinates")
    void shouldHandleAddressWithCoordinates() {
        // Given
        Partner.Address address = new Partner.Address();
        address.setLatitude(40.7128);
        address.setLongitude(-74.0060);

        // Then
        assertEquals(40.7128, address.getLatitude());
        assertEquals(-74.0060, address.getLongitude());
    }

    @Test
    @DisplayName("Should update timestamp on status change")
    void shouldUpdateTimestampOnStatusChange() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");
        var initialUpdatedAt = partner.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        // When
        partner.activate();

        // Then
        assertTrue(partner.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    @DisplayName("Should support full partner lifecycle")
    void shouldSupportFullPartnerLifecycle() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");

        // Initial state
        assertEquals(Partner.PartnerStatus.PENDING, partner.getStatus());
        assertEquals(Partner.VerificationStatus.PENDING, partner.getVerificationStatus());

        // Verify
        partner.verify();
        assertEquals(Partner.VerificationStatus.VERIFIED, partner.getVerificationStatus());

        // Activate
        partner.activate();
        assertEquals(Partner.PartnerStatus.ACTIVE, partner.getStatus());

        // Update rating
        partner.updateRating(4.8);
        assertEquals(4.8, partner.getRating());

        // Deactivate
        partner.deactivate();
        assertEquals(Partner.PartnerStatus.INACTIVE, partner.getStatus());
    }

    @Test
    @DisplayName("Should handle rejection with reason")
    void shouldHandleRejectionWithReason() {
        // Given
        Partner partner = new Partner("tenant-001", "PARTNER-001", "Acme Logistics", "contact@acme.com");

        // When
        partner.rejectVerification("Invalid business license");

        // Then
        assertEquals(Partner.VerificationStatus.REJECTED, partner.getVerificationStatus());
        // Note: The rejection reason is not stored in the entity in current implementation
        // In real scenario, you might want to add a rejectionReason field
    }
}
