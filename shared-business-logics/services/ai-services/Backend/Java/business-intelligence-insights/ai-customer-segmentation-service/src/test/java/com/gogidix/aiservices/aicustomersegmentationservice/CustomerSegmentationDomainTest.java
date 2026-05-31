package com.gogidix.aiservices.aicustomersegmentationservice;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.event.CustomersAddedToSegmentEvent;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.event.DomainEvent;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.event.SegmentCreatedEvent;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.event.SegmentDeletedEvent;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.event.SegmentUpdatedEvent;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.policy.MaxSegmentsPerTenantPolicy;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.policy.MinimumCustomerCountPolicy;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.policy.SegmentNameUniquePolicy;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.BaseDomainException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ConflictException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ErrorResponse;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.util.TenantIdGenerator;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Segmentation Domain Tests")
class CustomerSegmentationDomainTest {

    @Nested
    @DisplayName("MaxSegmentsPerTenantPolicy Tests")
    class MaxSegmentsPerTenantPolicyTest {

        @Test
        @DisplayName("Should use default max of 100")
        void shouldUseDefaultMax() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy();
            assertEquals(100, policy.getMaxSegments());
        }

        @Test
        @DisplayName("Should accept custom max value")
        void shouldAcceptCustomMax() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(50);
            assertEquals(50, policy.getMaxSegments());
        }

        @Test
        @DisplayName("Should reject zero or negative max")
        void shouldRejectZeroOrNegative() {
            assertThrows(IllegalArgumentException.class, () -> new MaxSegmentsPerTenantPolicy(0));
            assertThrows(IllegalArgumentException.class, () -> new MaxSegmentsPerTenantPolicy(-1));
        }

        @Test
        @DisplayName("Should validate successfully when under limit")
        void shouldValidateWhenUnderLimit() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(10);
            assertDoesNotThrow(() -> policy.validate(5, "tenant-1"));
        }

        @Test
        @DisplayName("Should throw when at limit")
        void shouldThrowWhenAtLimit() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(10);
            assertThrows(BusinessException.class, () -> policy.validate(10, "tenant-1"));
        }

        @Test
        @DisplayName("Should throw when over limit")
        void shouldThrowWhenOverLimit() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(5);
            assertThrows(BusinessException.class, () -> policy.validate(10, "tenant-1"));
        }

        @Test
        @DisplayName("canCreateSegment returns true when under limit")
        void canCreateWhenUnderLimit() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(10);
            assertTrue(policy.canCreateSegment(5));
            assertTrue(policy.canCreateSegment(9));
        }

        @Test
        @DisplayName("canCreateSegment returns false when at or over limit")
        void cannotCreateWhenAtLimit() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(10);
            assertFalse(policy.canCreateSegment(10));
            assertFalse(policy.canCreateSegment(15));
        }

        @Test
        @DisplayName("getRemainingSegments calculates correctly")
        void remainingSegments() {
            MaxSegmentsPerTenantPolicy policy = new MaxSegmentsPerTenantPolicy(100);
            assertEquals(90, policy.getRemainingSegments(10));
            assertEquals(0, policy.getRemainingSegments(100));
            assertEquals(0, policy.getRemainingSegments(150));
            assertEquals(100, policy.getRemainingSegments(0));
        }
    }

    @Nested
    @DisplayName("MinimumCustomerCountPolicy Tests")
    class MinimumCustomerCountPolicyTest {

        @Test
        @DisplayName("Should use default minimum of 10")
        void shouldUseDefaultMin() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy();
            assertEquals(10, policy.getMinimumCustomerCount());
        }

        @Test
        @DisplayName("Should accept custom minimum")
        void shouldAcceptCustomMin() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(20);
            assertEquals(20, policy.getMinimumCustomerCount());
        }

        @Test
        @DisplayName("Should reject negative minimum")
        void shouldRejectNegative() {
            assertThrows(IllegalArgumentException.class, () -> new MinimumCustomerCountPolicy(-1));
        }

        @Test
        @DisplayName("Should accept zero minimum")
        void shouldAcceptZero() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(0);
            assertEquals(0, policy.getMinimumCustomerCount());
        }

        @Test
        @DisplayName("validateForActivation passes when count meets threshold")
        void activationPassesWhenEnough() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertDoesNotThrow(() -> policy.validateForActivation(10, "seg-1"));
            assertDoesNotThrow(() -> policy.validateForActivation(50, "seg-1"));
        }

        @Test
        @DisplayName("validateForActivation throws when count below threshold")
        void activationFailsWhenNotEnough() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertThrows(BusinessException.class, () -> policy.validateForActivation(5, "seg-1"));
            assertThrows(BusinessException.class, () -> policy.validateForActivation(0, "seg-1"));
        }

        @Test
        @DisplayName("validateForAnalysis uses half the minimum (at least 1)")
        void analysisThresholdIsHalf() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertDoesNotThrow(() -> policy.validateForAnalysis(5, "seg-1"));
            assertThrows(ValidationException.class, () -> policy.validateForAnalysis(4, "seg-1"));
        }

        @Test
        @DisplayName("canActivate returns correct result")
        void canActivate() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertTrue(policy.canActivate(10));
            assertTrue(policy.canActivate(20));
            assertFalse(policy.canActivate(9));
            assertFalse(policy.canActivate(0));
        }

        @Test
        @DisplayName("getAdditionalCustomersNeeded calculates correctly")
        void additionalCustomersNeeded() {
            MinimumCustomerCountPolicy policy = new MinimumCustomerCountPolicy(10);
            assertEquals(5, policy.getAdditionalCustomersNeeded(5));
            assertEquals(0, policy.getAdditionalCustomersNeeded(10));
            assertEquals(0, policy.getAdditionalCustomersNeeded(20));
            assertEquals(10, policy.getAdditionalCustomersNeeded(0));
        }
    }

    @Nested
    @DisplayName("SegmentNameUniquePolicy Tests")
    class SegmentNameUniquePolicyTest {

        @Test
        @DisplayName("validate passes when name is unique")
        void validatePassesForUniqueName() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertDoesNotThrow(() -> policy.validate(List.of("Alpha", "Beta"), "Gamma", "t1"));
        }

        @Test
        @DisplayName("validate throws for duplicate name (case insensitive)")
        void validateThrowsForDuplicate() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertThrows(ConflictException.class,
                    () -> policy.validate(List.of("Alpha", "Beta"), "alpha", "t1"));
        }

        @Test
        @DisplayName("validate passes when existing names is null")
        void validatePassesForNullList() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertDoesNotThrow(() -> policy.validate(null, "Alpha", "t1"));
        }

        @Test
        @DisplayName("isUnique returns true for unique name")
        void isUniqueTrueForUnique() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertTrue(policy.isUnique(List.of("Alpha"), "Beta"));
        }

        @Test
        @DisplayName("isUnique returns false for duplicate")
        void isUniqueFalseForDuplicate() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertFalse(policy.isUnique(List.of("Alpha"), "ALPHA"));
        }

        @Test
        @DisplayName("isUnique returns false for null inputs")
        void isUniqueFalseForNull() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertFalse(policy.isUnique(null, "Alpha"));
            assertFalse(policy.isUnique(List.of("Alpha"), null));
        }

        @Test
        @DisplayName("generateUniqueName returns base when unique")
        void generateReturnsBaseWhenUnique() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            assertEquals("Alpha", policy.generateUniqueName("Alpha", List.of("Beta")));
        }

        @Test
        @DisplayName("generateUniqueName appends counter when not unique")
        void generateAppendsCounter() {
            SegmentNameUniquePolicy policy = new SegmentNameUniquePolicy();
            String result = policy.generateUniqueName("Alpha", List.of("Alpha", "Alpha_1"));
            assertEquals("Alpha_2", result);
        }
    }

    @Nested
    @DisplayName("SegmentCreatedEvent Tests")
    class SegmentCreatedEventTest {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            SegmentCreatedEvent event = new SegmentCreatedEvent("agg-1", "t-1", "VIP", "BEHAVIORAL", now);

            assertNotNull(event.getEventId());
            assertEquals("agg-1", event.getAggregateId());
            assertEquals("t-1", event.getTenantId());
            assertEquals("VIP", event.getSegmentName());
            assertEquals("BEHAVIORAL", event.getSegmentType());
            assertEquals(now, event.getOccurredAt());
        }

        @Test
        @DisplayName("Should implement DomainEvent with correct eventType")
        void shouldImplementDomainEvent() {
            SegmentCreatedEvent event = new SegmentCreatedEvent("a", "t", "n", "s", Instant.now());
            assertInstanceOf(DomainEvent.class, event);
            assertEquals("SegmentCreatedEvent", event.getEventType());
        }

        @Test
        @DisplayName("Should reject null required fields")
        void shouldRejectNulls() {
            Instant now = Instant.now();
            assertThrows(NullPointerException.class, () -> new SegmentCreatedEvent(null, "t", "n", "s", now));
            assertThrows(NullPointerException.class, () -> new SegmentCreatedEvent("a", null, "n", "s", now));
            assertThrows(NullPointerException.class, () -> new SegmentCreatedEvent("a", "t", null, "s", now));
            assertThrows(NullPointerException.class, () -> new SegmentCreatedEvent("a", "t", "n", null, now));
            assertThrows(NullPointerException.class, () -> new SegmentCreatedEvent("a", "t", "n", "s", null));
        }

        @Test
        @DisplayName("Should be equal by eventId")
        void shouldBeEqualByEventId() {
            SegmentCreatedEvent e1 = new SegmentCreatedEvent("a", "t", "n", "s", Instant.now());
            assertNotEquals(e1, new SegmentCreatedEvent("a", "t", "n", "s", Instant.now()));
            assertEquals(e1, e1);
        }

        @Test
        @DisplayName("toString should contain key fields")
        void toStringShouldContainFields() {
            SegmentCreatedEvent event = new SegmentCreatedEvent("a", "t", "VIP", "s", Instant.now());
            String s = event.toString();
            assertTrue(s.contains("VIP"));
            assertTrue(s.contains("SegmentCreatedEvent"));
        }
    }

    @Nested
    @DisplayName("SegmentDeletedEvent Tests")
    class SegmentDeletedEventTest {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            SegmentDeletedEvent event = new SegmentDeletedEvent("agg-1", "t-1", "VIP", "HARD", 50L, now);

            assertNotNull(event.getEventId());
            assertEquals("agg-1", event.getAggregateId());
            assertEquals("VIP", event.getSegmentName());
            assertEquals("HARD", event.getDeletionType());
            assertEquals(50L, event.getCustomerCount());
            assertEquals(now, event.getOccurredAt());
        }

        @Test
        @DisplayName("Should accept null customerCount")
        void shouldAcceptNullCustomerCount() {
            SegmentDeletedEvent event = new SegmentDeletedEvent("a", "t", "n", "HARD", null, Instant.now());
            assertNull(event.getCustomerCount());
        }

        @Test
        @DisplayName("Should reject null required fields")
        void shouldRejectNulls() {
            assertThrows(NullPointerException.class,
                    () -> new SegmentDeletedEvent(null, "t", "n", "d", 1L, Instant.now()));
            assertThrows(NullPointerException.class,
                    () -> new SegmentDeletedEvent("a", "t", "n", null, 1L, Instant.now()));
        }
    }

    @Nested
    @DisplayName("SegmentUpdatedEvent Tests")
    class SegmentUpdatedEventTest {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            SegmentUpdatedEvent event = new SegmentUpdatedEvent("agg-1", "t-1", "OldName", "NewName", "RENAME", now);

            assertEquals("agg-1", event.getAggregateId());
            assertEquals("OldName", event.getOldName());
            assertEquals("NewName", event.getNewName());
            assertEquals("RENAME", event.getChangeType());
        }

        @Test
        @DisplayName("Should accept null oldName")
        void shouldAcceptNullOldName() {
            SegmentUpdatedEvent event = new SegmentUpdatedEvent("a", "t", null, "new", "c", Instant.now());
            assertNull(event.getOldName());
        }

        @Test
        @DisplayName("Should reject null newName and changeType")
        void shouldRejectNulls() {
            assertThrows(NullPointerException.class,
                    () -> new SegmentUpdatedEvent("a", "t", "old", null, "c", Instant.now()));
            assertThrows(NullPointerException.class,
                    () -> new SegmentUpdatedEvent("a", "t", "old", "new", null, Instant.now()));
        }
    }

    @Nested
    @DisplayName("CustomersAddedToSegmentEvent Tests")
    class CustomersAddedToSegmentEventTest {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            CustomersAddedToSegmentEvent event = new CustomersAddedToSegmentEvent("agg-1", "t-1", "VIP", 5, 100L, now);

            assertEquals("agg-1", event.getAggregateId());
            assertEquals("VIP", event.getSegmentName());
            assertEquals(5, event.getCustomersAdded());
            assertEquals(100L, event.getTotalCustomerCount());
        }

        @Test
        @DisplayName("Should reject null required fields")
        void shouldRejectNulls() {
            assertThrows(NullPointerException.class,
                    () -> new CustomersAddedToSegmentEvent(null, "t", "n", 1, 1L, Instant.now()));
            assertThrows(NullPointerException.class,
                    () -> new CustomersAddedToSegmentEvent("a", "t", "n", null, 1L, Instant.now()));
            assertThrows(NullPointerException.class,
                    () -> new CustomersAddedToSegmentEvent("a", "t", "n", 1, null, Instant.now()));
        }
    }

    @Nested
    @DisplayName("BaseDomainException Tests")
    class BaseDomainExceptionTest {

        @Test
        @DisplayName("Should derive error code from class name")
        void shouldDeriveErrorCode() {
            BusinessException ex = new BusinessException("test message");
            assertEquals("BUSINESS_RULE_VIOLATION", ex.getErrorCode());
        }

        @Test
        @DisplayName("Should accept custom error code")
        void shouldAcceptCustomErrorCode() {
            BusinessException ex = new BusinessException("msg", "CUSTOM_CODE");
            assertEquals("CUSTOM_CODE", ex.getErrorCode());
        }

        @Test
        @DisplayName("Should accept cause")
        void shouldAcceptCause() {
            RuntimeException cause = new RuntimeException("root cause");
            BusinessException ex = new BusinessException("msg", cause);
            assertEquals("msg", ex.getMessage());
            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("Should accept both error code and cause")
        void shouldAcceptErrorCodeAndCause() {
            RuntimeException cause = new RuntimeException("root");
            BusinessException ex = new BusinessException("msg", "CODE", cause);
            assertEquals("CODE", ex.getErrorCode());
            assertEquals(cause, ex.getCause());
        }
    }

    @Nested
    @DisplayName("BusinessException Tests")
    class BusinessExceptionTest {

        @Test
        @DisplayName("Should create with message")
        void shouldCreateWithMessage() {
            BusinessException ex = new BusinessException("error occurred");
            assertEquals("error occurred", ex.getMessage());
            assertEquals("BUSINESS_RULE_VIOLATION", ex.getErrorCode());
        }

        @Test
        @DisplayName("Should create with message and error code")
        void shouldCreateWithMessageAndCode() {
            BusinessException ex = new BusinessException("msg", "MAX_SEGMENTS_EXCEEDED");
            assertEquals("MAX_SEGMENTS_EXCEEDED", ex.getErrorCode());
        }
    }

    @Nested
    @DisplayName("ValidationException Tests")
    class ValidationExceptionTest {

        @Test
        @DisplayName("Should create with single message")
        void shouldCreateWithMessage() {
            ValidationException ex = new ValidationException("field is invalid");
            assertEquals("field is invalid", ex.getMessage());
            assertEquals(List.of("field is invalid"), ex.getValidationErrors());
        }

        @Test
        @DisplayName("Should create with list of errors")
        void shouldCreateWithErrorList() {
            List<String> errors = List.of("error1", "error2");
            ValidationException ex = new ValidationException(errors);
            assertEquals("error1, error2", ex.getMessage());
            assertEquals(errors, ex.getValidationErrors());
        }

        @Test
        @DisplayName("Should create with message and error list")
        void shouldCreateWithMessageAndErrorList() {
            List<String> errors = List.of("e1", "e2");
            ValidationException ex = new ValidationException("Validation failed", errors);
            assertEquals("Validation failed", ex.getMessage());
            assertEquals(errors, ex.getValidationErrors());
        }

        @Test
        @DisplayName("deriveErrorCode should return VALIDATION_FAILED")
        void shouldDeriveErrorCode() {
            ValidationException ex = new ValidationException("test");
            assertEquals("VALIDATION_FAILED", ex.getErrorCode());
        }
    }

    @Nested
    @DisplayName("ConflictException Tests")
    class ConflictExceptionTest {

        @Test
        @DisplayName("Should create with resource type and value")
        void shouldCreateWithTypeAndValue() {
            ConflictException ex = new ConflictException("CustomerSegment", "VIP");
            assertTrue(ex.getMessage().contains("VIP"));
            assertEquals("CustomerSegment", ex.getResourceType());
            assertEquals("VIP", ex.getConflictingValue());
        }

        @Test
        @DisplayName("Should create with message only")
        void shouldCreateWithMessage() {
            ConflictException ex = new ConflictException("conflict!");
            assertEquals("conflict!", ex.getMessage());
            assertEquals("RESOURCE", ex.getResourceType());
            assertEquals("UNKNOWN", ex.getConflictingValue());
        }

        @Test
        @DisplayName("Should create with resource type, value, and error code")
        void shouldCreateWithErrorCode() {
            ConflictException ex = new ConflictException("Type", "Val", "CUSTOM_CODE");
            assertEquals("CUSTOM_CODE", ex.getErrorCode());
            assertEquals("Type", ex.getResourceType());
            assertEquals("Val", ex.getConflictingValue());
        }
    }

    @Nested
    @DisplayName("NotFoundException Tests")
    class NotFoundExceptionTest {

        @Test
        @DisplayName("Should create with type and id")
        void shouldCreateWithTypeAndId() {
            NotFoundException ex = new NotFoundException("CustomerSegment", "seg-123");
            assertTrue(ex.getMessage().contains("seg-123"));
            assertEquals("CustomerSegment", ex.getResourceType());
            assertEquals("seg-123", ex.getResourceId());
        }

        @Test
        @DisplayName("Should create with message only")
        void shouldCreateWithMessage() {
            NotFoundException ex = new NotFoundException("not found!");
            assertEquals("not found!", ex.getMessage());
            assertEquals("RESOURCE", ex.getResourceType());
        }

        @Test
        @DisplayName("Should create with message and cause")
        void shouldCreateWithMessageAndCause() {
            RuntimeException cause = new RuntimeException("db error");
            NotFoundException ex = new NotFoundException("not found", cause);
            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("deriveErrorCode should return RESOURCE_NOT_FOUND")
        void shouldDeriveErrorCode() {
            NotFoundException ex = new NotFoundException("test");
            assertEquals("RESOURCE_NOT_FOUND", ex.getErrorCode());
        }
    }

    @Nested
    @DisplayName("ErrorResponse Tests")
    class ErrorResponseTest {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            Instant now = Instant.now();
            ErrorResponse response = ErrorResponse.builder()
                    .timestamp(now)
                    .status(400)
                    .error("BAD_REQUEST")
                    .message("Invalid input")
                    .path("/api/segments")
                    .correlationId("corr-1")
                    .tenantId("t-1")
                    .details(List.of("field1 is required"))
                    .build();

            assertEquals(now, response.getTimestamp());
            assertEquals(400, response.getStatus());
            assertEquals("BAD_REQUEST", response.getError());
            assertEquals("Invalid input", response.getMessage());
            assertEquals("/api/segments", response.getPath());
            assertEquals("corr-1", response.getCorrelationId());
            assertEquals("t-1", response.getTenantId());
            assertEquals(List.of("field1 is required"), response.getDetails());
        }

        @Test
        @DisplayName("Should default timestamp to now when null")
        void shouldDefaultTimestamp() {
            ErrorResponse response = ErrorResponse.builder()
                    .status(500)
                    .error("ERROR")
                    .message("fail")
                    .path("/test")
                    .correlationId("c")
                    .tenantId("t")
                    .build();

            assertNotNull(response.getTimestamp());
        }

        @Test
        @DisplayName("fromException with BaseDomainException extracts error code")
        void fromExceptionWithDomainException() {
            BusinessException ex = new BusinessException("business error", "CUSTOM_CODE");
            ErrorResponse response = ErrorResponse.fromException(ex, "/api/test", 422)
                    .correlationId("c")
                    .tenantId("t")
                    .build();

            assertEquals(422, response.getStatus());
            assertEquals("CUSTOM_CODE", response.getError());
            assertEquals("business error", response.getMessage());
            assertEquals("/api/test", response.getPath());
        }

        @Test
        @DisplayName("fromException with generic exception uses INTERNAL_SERVER_ERROR")
        void fromExceptionWithGenericException() {
            RuntimeException ex = new RuntimeException("generic error");
            ErrorResponse response = ErrorResponse.fromException(ex, "/api/test", 500)
                    .correlationId("c")
                    .tenantId("t")
                    .build();

            assertEquals(500, response.getStatus());
            assertEquals("INTERNAL_SERVER_ERROR", response.getError());
        }
    }

    @Nested
    @DisplayName("ValidationUtil Tests")
    class ValidationUtilTest {

        @Test
        @DisplayName("isNotEmpty returns true for non-blank string")
        void isNotEmptyTrue() {
            assertTrue(ValidationUtil.isNotEmpty("hello"));
            assertTrue(ValidationUtil.isNotEmpty("  a  "));
        }

        @Test
        @DisplayName("isNotEmpty returns false for null, empty, blank")
        void isNotEmptyFalse() {
            assertFalse(ValidationUtil.isNotEmpty(null));
            assertFalse(ValidationUtil.isNotEmpty(""));
            assertFalse(ValidationUtil.isNotEmpty("   "));
        }

        @Test
        @DisplayName("isEmpty returns true for null, empty, blank")
        void isEmptyTrue() {
            assertTrue(ValidationUtil.isEmpty(null));
            assertTrue(ValidationUtil.isEmpty(""));
            assertTrue(ValidationUtil.isEmpty("   "));
        }

        @Test
        @DisplayName("isEmpty returns false for non-blank")
        void isEmptyFalse() {
            assertFalse(ValidationUtil.isEmpty("hello"));
        }

        @Test
        @DisplayName("isValidEmail returns false for null")
        void isValidEmailNull() {
            assertFalse(ValidationUtil.isValidEmail(null));
        }
    }

    @Nested
    @DisplayName("TenantIdGenerator Tests")
    class TenantIdGeneratorTest {

        @Test
        @DisplayName("generate should return tenant_ prefixed ID")
        void generateShouldReturnPrefixed() {
            String id = TenantIdGenerator.generate();
            assertTrue(id.startsWith("tenant_"));
            assertTrue(id.length() > "tenant_".length());
        }

        @Test
        @DisplayName("generate should return unique IDs")
        void generateShouldReturnUnique() {
            String id1 = TenantIdGenerator.generate();
            String id2 = TenantIdGenerator.generate();
            assertNotEquals(id1, id2);
        }

        @Test
        @DisplayName("isValid returns true for generated IDs")
        void isValidForGenerated() {
            String id = TenantIdGenerator.generate();
            assertTrue(TenantIdGenerator.isValid(id));
        }

        @Test
        @DisplayName("isValid returns false for invalid inputs")
        void isValidFalseForInvalid() {
            assertFalse(TenantIdGenerator.isValid(null));
            assertFalse(TenantIdGenerator.isValid(""));
            assertFalse(TenantIdGenerator.isValid("   "));
            assertFalse(TenantIdGenerator.isValid("prefix_something"));
            assertFalse(TenantIdGenerator.isValid("tenant_"));
        }

        @Test
        @DisplayName("extractUuid returns UUID portion")
        void extractUuid() {
            String id = TenantIdGenerator.generate();
            String uuid = TenantIdGenerator.extractUuid(id);
            assertNotNull(uuid);
            assertTrue(uuid.length() > 0);
            assertFalse(uuid.startsWith("tenant_"));
        }

        @Test
        @DisplayName("extractUuid returns null for invalid input")
        void extractUuidNullForInvalid() {
            assertNull(TenantIdGenerator.extractUuid(null));
            assertNull(TenantIdGenerator.extractUuid("invalid"));
        }
    }
}
