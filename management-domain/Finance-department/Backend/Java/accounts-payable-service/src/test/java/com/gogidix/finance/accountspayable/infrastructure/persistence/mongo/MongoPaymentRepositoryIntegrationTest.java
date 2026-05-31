package com.gogidix.finance.accountspayable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountspayable.AbstractMongoDBTestContainer;
import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive integration tests for MongoPaymentRepository using MongoDB Testcontainers
 * Tests all CRUD operations and queries with 80%+ coverage target
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
class MongoPaymentRepositoryIntegrationTest extends AbstractMongoDBTestContainer {

    @Autowired
    private MongoPaymentRepository paymentRepository;

    private static final String TENANT_ID = "tenant-123";
    private static final String VENDOR_ID = "vendor-456";
    private static final String VENDOR_NAME = "Acme Supplies";

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    // SAVE Tests
    @Test
    void testSave_Success() {
        Payment payment = createTestPayment();

        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved.getId());
        assertNotNull(saved.getPaymentId());
        assertEquals(TENANT_ID, saved.getTenantId());
        assertEquals(VENDOR_ID, saved.getVendorId());
    }

    @Test
    void testSaveAll_Success() {
        Payment payment1 = createTestPayment();
        Payment payment2 = createTestPayment();

        List<Payment> saved = paymentRepository.saveAll(List.of(payment1, payment2));

        assertEquals(2, saved.size());
        assertNotNull(saved.get(0).getId());
        assertNotNull(saved.get(1).getId());
    }

    // FIND BY ID Tests
    @Test
    void testFindById_Success() {
        Payment payment = createAndSavePayment();

        Optional<Payment> result = paymentRepository.findById(payment.getId());

        assertTrue(result.isPresent());
        assertEquals(payment.getPaymentId(), result.get().getPaymentId());
    }

    // FIND BY PAYMENT ID AND TENANT Tests
    @Test
    void testFindByPaymentIdAndTenantId_Success() {
        Payment payment = createAndSavePayment();

        Optional<Payment> result = paymentRepository.findByPaymentIdAndTenantId(
            payment.getPaymentId(), TENANT_ID);

        assertTrue(result.isPresent());
        assertEquals(payment.getPaymentId(), result.get().getPaymentId());
    }

    @Test
    void testFindByPaymentIdAndTenantId_NotFound_ReturnsEmpty() {
        Optional<Payment> result = paymentRepository.findByPaymentIdAndTenantId(
            "non-existent", TENANT_ID);

        assertFalse(result.isPresent());
    }

    // FIND BY TENANT Tests
    @Test
    void testFindByTenantId_Success() {
        createAndSavePayment();
        createAndSavePayment();
        createAndSavePayment();

        List<Payment> result = paymentRepository.findByTenantId(TENANT_ID);

        assertEquals(3, result.size());
    }

    // FIND BY TENANT AND VENDOR Tests
    @Test
    void testFindByTenantIdAndVendorId_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createTestPayment();
        p3.setVendorId("other-vendor");
        paymentRepository.save(p3);

        List<Payment> result = paymentRepository.findByTenantIdAndVendorId(TENANT_ID, VENDOR_ID);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> VENDOR_ID.equals(p.getVendorId())));
    }

    // FIND BY TENANT AND STATUS Tests
    @Test
    void testFindByTenantIdAndStatus_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setStatus(Payment.PaymentStatus.PENDING);
        p2.setStatus(Payment.PaymentStatus.COMPLETED);
        p3.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findByTenantIdAndStatus(
            TENANT_ID, Payment.PaymentStatus.PENDING);

        assertEquals(2, result.size());
    }

    // FIND BY TENANT AND PAYMENT METHOD Tests
    @Test
    void testFindByTenantIdAndPaymentMethod_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        p2.setPaymentMethod(Payment.PaymentMethod.ACH);
        p3.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findByTenantIdAndPaymentMethod(
            TENANT_ID, Payment.PaymentMethod.BANK_TRANSFER);

        assertEquals(2, result.size());
    }

    // FIND BY TENANT AND DATE RANGE Tests
    @Test
    void testFindByTenantIdAndPaymentDateBetween_Success() {
        LocalDate baseDate = LocalDate.now();
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setPaymentDate(baseDate.minusDays(10));
        p2.setPaymentDate(baseDate);
        p3.setPaymentDate(baseDate.plusDays(10));
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findByTenantIdAndPaymentDateBetween(
            TENANT_ID, baseDate.minusDays(5), baseDate.plusDays(5));

        assertEquals(2, result.size());
    }

    // FIND PENDING PAYMENTS Tests
    @Test
    void testFindPendingPayments_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setStatus(Payment.PaymentStatus.PENDING);
        p2.setStatus(Payment.PaymentStatus.SCHEDULED);
        p3.setStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findPendingPayments(TENANT_ID);

        assertEquals(1, result.size());
        assertEquals(Payment.PaymentStatus.PENDING, result.get(0).getStatus());
    }

    // FIND SCHEDULED PAYMENTS Tests
    @Test
    void testFindScheduledPayments_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        p1.setStatus(Payment.PaymentStatus.SCHEDULED);
        p1.setScheduledDate(LocalDate.now().plusDays(5));
        p2.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.saveAll(List.of(p1, p2));

        List<Payment> result = paymentRepository.findScheduledPayments(TENANT_ID);

        assertEquals(1, result.size());
        assertEquals(Payment.PaymentStatus.SCHEDULED, result.get(0).getStatus());
    }

    // FIND PAYMENTS DUE FOR PROCESSING Tests
    @Test
    void testFindPaymentsDueForProcessing_Success() {
        LocalDate today = LocalDate.now();
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setStatus(Payment.PaymentStatus.SCHEDULED);
        p1.setScheduledDate(today);
        p2.setStatus(Payment.PaymentStatus.SCHEDULED);
        p2.setScheduledDate(today.minusDays(1));
        p3.setStatus(Payment.PaymentStatus.SCHEDULED);
        p3.setScheduledDate(today.plusDays(5));
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findPaymentsDueForProcessing(TENANT_ID, today);

        assertEquals(2, result.size());
    }

    // FIND BY INVOICE ID Tests
    @Test
    void testFindByInvoiceId_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        p1.setInvoiceIds(List.of("INV-001", "INV-002"));
        p2.setInvoiceIds(List.of("INV-003"));
        paymentRepository.saveAll(List.of(p1, p2));

        List<Payment> result = paymentRepository.findByInvoiceId(TENANT_ID, "INV-001");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getInvoiceIds().contains("INV-001"));
    }

    // FIND BY AMOUNT RANGE Tests
    @Test
    void testFindByTenantIdAndAmountBetween_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setAmount(new BigDecimal("100.00"));
        p2.setAmount(new BigDecimal("500.00"));
        p3.setAmount(new BigDecimal("1000.00"));
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findByTenantIdAndAmountBetween(
            TENANT_ID, new BigDecimal("200.00"), new BigDecimal("800.00"));

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500.00"), result.get(0).getAmount());
    }

    // FIND BY CURRENCY Tests
    @Test
    void testFindByTenantIdAndCurrency_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setCurrency("USD");
        p2.setCurrency("EUR");
        p3.setCurrency("USD");
        paymentRepository.saveAll(List.of(p1, p2, p3));

        List<Payment> result = paymentRepository.findByTenantIdAndCurrency(TENANT_ID, "USD");

        assertEquals(2, result.size());
    }

    // FIND BY BATCH ID Tests
    @Test
    void testFindByBatchId_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        p1.setBatchId("BATCH-001");
        p2.setBatchId("BATCH-001");
        paymentRepository.saveAll(List.of(p1, p2));

        List<Payment> result = paymentRepository.findByBatchId("BATCH-001");

        assertEquals(2, result.size());
    }

    // EXISTS Tests
    @Test
    void testExistsByPaymentIdAndTenantId_True() {
        Payment payment = createAndSavePayment();

        boolean result = paymentRepository.existsByPaymentIdAndTenantId(
            payment.getPaymentId(), TENANT_ID);

        assertTrue(result);
    }

    @Test
    void testExistsByPaymentIdAndTenantId_False() {
        boolean result = paymentRepository.existsByPaymentIdAndTenantId(
            "non-existent", TENANT_ID);

        assertFalse(result);
    }

    // DELETE Tests
    @Test
    void testDeleteById_Success() {
        Payment payment = createAndSavePayment();

        paymentRepository.deleteById(payment.getId());

        Optional<Payment> result = paymentRepository.findById(payment.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteByPaymentIdAndTenantId_Success() {
        Payment payment = createAndSavePayment();

        paymentRepository.deleteByPaymentIdAndTenantId(payment.getPaymentId(), TENANT_ID);

        Optional<Payment> result = paymentRepository.findByPaymentIdAndTenantId(
            payment.getPaymentId(), TENANT_ID);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteAllByTenantId_Success() {
        createAndSavePayment();
        createAndSavePayment();
        createAndSavePayment();

        paymentRepository.deleteAllByTenantId(TENANT_ID);

        List<Payment> result = paymentRepository.findByTenantId(TENANT_ID);
        assertEquals(0, result.size());
    }

    // COUNT Tests
    @Test
    void testCountByTenantId_Success() {
        createAndSavePayment();
        createAndSavePayment();
        createAndSavePayment();

        long count = paymentRepository.countByTenantId(TENANT_ID);

        assertEquals(3, count);
    }

    @Test
    void testCountByTenantIdAndStatus_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        Payment p3 = createAndSavePayment();
        p1.setStatus(Payment.PaymentStatus.COMPLETED);
        p2.setStatus(Payment.PaymentStatus.COMPLETED);
        p3.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.saveAll(List.of(p1, p2, p3));

        long count = paymentRepository.countByTenantIdAndStatus(
            TENANT_ID, Payment.PaymentStatus.COMPLETED);

        assertEquals(2, count);
    }

    // SUM AMOUNT Tests
    @Test
    void testSumAmountByTenantIdAndStatus_Success() {
        Payment p1 = createAndSavePayment();
        Payment p2 = createAndSavePayment();
        p1.setAmount(new BigDecimal("1000.00"));
        p2.setAmount(new BigDecimal("2000.00"));
        p1.setStatus(Payment.PaymentStatus.COMPLETED);
        p2.setStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.saveAll(List.of(p1, p2));

        BigDecimal sum = paymentRepository.sumAmountByTenantIdAndStatus(
            TENANT_ID, Payment.PaymentStatus.COMPLETED);

        assertEquals(new BigDecimal("3000.00"), sum);
    }

    // Helper methods
    private Payment createTestPayment() {
        return Payment.builder()
            .tenantId(TENANT_ID)
            .vendorId(VENDOR_ID)
            .vendorName(VENDOR_NAME)
            .amount(new BigDecimal("1000.00"))
            .currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .paymentMethod(Payment.PaymentMethod.BANK_TRANSFER)
            .invoiceIds(new ArrayList<>())
            .allocations(new ArrayList<>())
            .build();
    }

    private Payment createAndSavePayment() {
        return paymentRepository.save(createTestPayment());
    }
}
