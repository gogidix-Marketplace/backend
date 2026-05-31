package com.gogidix.finance.accountspayable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountspayable.AbstractMongoDBTestContainer;
import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContext;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
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
 * Comprehensive integration tests for MongoInvoiceRepository using MongoDB Testcontainers
 * Tests all CRUD operations and queries with 80%+ coverage target
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
class MongoInvoiceRepositoryIntegrationTest extends AbstractMongoDBTestContainer {

    @Autowired
    private MongoInvoiceRepository invoiceRepository;

    private static final String TENANT_ID = "tenant-123";
    private static final String VENDOR_ID = "vendor-456";
    private static final String VENDOR_NAME = "Acme Supplies";

    @BeforeEach
    void setUp() {
        setTenantContext(TENANT_ID);
        invoiceRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    // SAVE Tests
    @Test
    void testSave_Success() {
        Invoice invoice = createTestInvoice("INV-001");

        Invoice saved = invoiceRepository.save(invoice);

        assertNotNull(saved.getId());
        assertNotNull(saved.getInvoiceId());
        assertEquals("INV-001", saved.getInvoiceNumber());
    }

    @Test
    void testSaveAll_Success() {
        Invoice invoice1 = createTestInvoice("INV-001");
        Invoice invoice2 = createTestInvoice("INV-002");

        List<Invoice> saved = invoiceRepository.saveAll(List.of(invoice1, invoice2));

        assertEquals(2, saved.size());
        assertNotNull(saved.get(0).getId());
        assertNotNull(saved.get(1).getId());
    }

    // FIND BY ID Tests
    @Test
    void testFindById_Success() {
        Invoice invoice = createAndSaveInvoice("INV-001");

        Optional<Invoice> result = invoiceRepository.findById(invoice.getId());

        assertTrue(result.isPresent());
        assertEquals("INV-001", result.get().getInvoiceNumber());
    }

    @Test
    void testFindById_WrongTenant_ReturnsEmpty() {
        Invoice invoice = createAndSaveInvoice("INV-001");
        setTenantContext("other-tenant");

        Optional<Invoice> result = invoiceRepository.findById(invoice.getId());

        assertFalse(result.isPresent());
    }

    // FIND BY INVOICE ID AND TENANT Tests
    @Test
    void testFindByInvoiceIdAndTenantId_Success() {
        Invoice invoice = createAndSaveInvoice("INV-001");

        Optional<Invoice> result = invoiceRepository.findByInvoiceIdAndTenantId(
            invoice.getInvoiceId(), TENANT_ID);

        assertTrue(result.isPresent());
        assertEquals("INV-001", result.get().getInvoiceNumber());
    }

    @Test
    void testFindByInvoiceIdAndTenantId_NotFound_ReturnsEmpty() {
        Optional<Invoice> result = invoiceRepository.findByInvoiceIdAndTenantId(
            "non-existent", TENANT_ID);

        assertFalse(result.isPresent());
    }

    // FIND BY INVOICE NUMBER AND TENANT Tests
    @Test
    void testFindByInvoiceNumberAndTenantId_Success() {
        createAndSaveInvoice("INV-001");

        Optional<Invoice> result = invoiceRepository.findByInvoiceNumberAndTenantId(
            "INV-001", TENANT_ID);

        assertTrue(result.isPresent());
        assertEquals("INV-001", result.get().getInvoiceNumber());
    }

    @Test
    void testFindByInvoiceNumberAndTenantId_DifferentTenant_ReturnsEmpty() {
        createAndSaveInvoice("INV-001");

        Optional<Invoice> result = invoiceRepository.findByInvoiceNumberAndTenantId(
            "INV-001", "other-tenant");

        assertFalse(result.isPresent());
    }

    // FIND BY TENANT Tests
    @Test
    void testFindByTenantId_Success() {
        createAndSaveInvoice("INV-001");
        createAndSaveInvoice("INV-002");
        createAndSaveInvoice("INV-003");

        List<Invoice> result = invoiceRepository.findByTenantId(TENANT_ID);

        assertEquals(3, result.size());
    }

    // FIND BY TENANT AND STATUS Tests
    @Test
    void testFindByTenantIdAndStatus_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv2.setStatus(Invoice.InvoiceStatus.DRAFT);
        invoiceRepository.saveAll(List.of(inv1, inv2));

        List<Invoice> result = invoiceRepository.findByTenantIdAndStatus(
            TENANT_ID, Invoice.InvoiceStatus.APPROVED);

        assertEquals(1, result.size());
        assertEquals(Invoice.InvoiceStatus.APPROVED, result.get(0).getStatus());
    }

    // FIND BY TENANT AND VENDOR Tests
    @Test
    void testFindByTenantIdAndVendorId_Success() {
        createAndSaveInvoiceWithVendor("INV-001", VENDOR_ID);
        createAndSaveInvoiceWithVendor("INV-002", VENDOR_ID);
        createAndSaveInvoiceWithVendor("INV-003", "other-vendor");

        List<Invoice> result = invoiceRepository.findByTenantIdAndVendorId(TENANT_ID, VENDOR_ID);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(inv -> VENDOR_ID.equals(inv.getVendorId())));
    }

    // FIND BY TENANT AND DEPARTMENT Tests
    @Test
    void testFindByTenantIdAndDepartment_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setDepartment("Finance");
        inv2.setDepartment("HR");
        invoiceRepository.saveAll(List.of(inv1, inv2));

        List<Invoice> result = invoiceRepository.findByTenantIdAndDepartment(TENANT_ID, "Finance");

        assertEquals(1, result.size());
        assertEquals("Finance", result.get(0).getDepartment());
    }

    // FIND BY TENANT AND COST CENTER Tests
    @Test
    void testFindByTenantIdAndCostCenter_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setCostCenter("CC-001");
        inv2.setCostCenter("CC-002");
        invoiceRepository.saveAll(List.of(inv1, inv2));

        List<Invoice> result = invoiceRepository.findByTenantIdAndCostCenter(TENANT_ID, "CC-001");

        assertEquals(1, result.size());
        assertEquals("CC-001", result.get(0).getCostCenter());
    }

    // FIND BY INVOICE DATE BETWEEN Tests
    @Test
    void testFindByTenantIdAndInvoiceDateBetween_Success() {
        LocalDate baseDate = LocalDate.now();
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setInvoiceDate(baseDate.minusDays(10));
        inv2.setInvoiceDate(baseDate);
        inv3.setInvoiceDate(baseDate.plusDays(10));
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findByTenantIdAndInvoiceDateBetween(
            TENANT_ID, baseDate.minusDays(5), baseDate.plusDays(5));

        assertEquals(2, result.size());
    }

    // FIND BY DUE DATE BETWEEN Tests
    @Test
    void testFindByTenantIdAndDueDateBetween_Success() {
        LocalDate baseDate = LocalDate.now();
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setDueDate(baseDate.minusDays(10));
        inv2.setDueDate(baseDate);
        inv3.setDueDate(baseDate.plusDays(10));
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findByTenantIdAndDueDateBetween(
            TENANT_ID, baseDate.minusDays(5), baseDate.plusDays(5));

        assertEquals(2, result.size());
    }

    // FIND BY STATUS IN Tests
    @Test
    void testFindByTenantIdAndStatusIn_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setStatus(Invoice.InvoiceStatus.DRAFT);
        inv2.setStatus(Invoice.InvoiceStatus.PENDING);
        inv3.setStatus(Invoice.InvoiceStatus.APPROVED);
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findByTenantIdAndStatusIn(
            TENANT_ID, List.of(Invoice.InvoiceStatus.DRAFT, Invoice.InvoiceStatus.APPROVED));

        assertEquals(2, result.size());
    }

    // FIND OVERDUE INVOICES Tests
    @Test
    void testFindOverdueInvoices_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setDueDate(LocalDate.now().minusDays(10));
        inv2.setDueDate(LocalDate.now().minusDays(5));
        inv3.setDueDate(LocalDate.now().plusDays(10));
        inv1.setStatus(Invoice.InvoiceStatus.PENDING);
        inv2.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv3.setStatus(Invoice.InvoiceStatus.APPROVED);
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findOverdueInvoices(TENANT_ID);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(inv -> inv.getDueDate().isBefore(LocalDate.now())));
    }

    // FIND INVOICES DUE FOR PAYMENT Tests
    @Test
    void testFindInvoicesDueForPayment_Success() {
        LocalDate today = LocalDate.now();
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setDueDate(today.minusDays(1));
        inv2.setDueDate(today);
        inv3.setDueDate(today.plusDays(5));
        inv1.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv2.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv3.setStatus(Invoice.InvoiceStatus.PENDING);
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findInvoicesDueForPayment(TENANT_ID, today);

        assertEquals(2, result.size());
    }

    // FIND BY TAGS Tests
    @Test
    void testFindByTenantIdAndTagsContaining_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setTags(List.of("urgent", "priority"));
        inv2.setTags(List.of("normal"));
        invoiceRepository.saveAll(List.of(inv1, inv2));

        List<Invoice> result = invoiceRepository.findByTenantIdAndTagsContaining(TENANT_ID, "urgent");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTags().contains("urgent"));
    }

    // SEARCH BY DESCRIPTION Tests
    @Test
    void testSearchByDescription_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setDescription("Office supplies for Q1");
        inv2.setDescription("IT equipment purchase");
        invoiceRepository.saveAll(List.of(inv1, inv2));

        List<Invoice> result = invoiceRepository.searchByDescription(TENANT_ID, "supplies");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getDescription().contains("supplies"));
    }

    // FIND BY AMOUNT BETWEEN Tests
    @Test
    void testFindByTenantIdAndAmountBetween_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setAmount(new BigDecimal("100.00"));
        inv2.setAmount(new BigDecimal("500.00"));
        inv3.setAmount(new BigDecimal("1000.00"));
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        List<Invoice> result = invoiceRepository.findByTenantIdAndAmountBetween(
            TENANT_ID, new BigDecimal("200.00"), new BigDecimal("800.00"));

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500.00"), result.get(0).getAmount());
    }

    // EXISTS Tests
    @Test
    void testExistsByInvoiceNumberAndTenantId_True() {
        createAndSaveInvoice("INV-001");

        boolean result = invoiceRepository.existsByInvoiceNumberAndTenantId("INV-001", TENANT_ID);

        assertTrue(result);
    }

    @Test
    void testExistsByInvoiceNumberAndTenantId_False() {
        boolean result = invoiceRepository.existsByInvoiceNumberAndTenantId("INV-999", TENANT_ID);

        assertFalse(result);
    }

    @Test
    void testExistsByInvoiceIdAndTenantId_True() {
        Invoice invoice = createAndSaveInvoice("INV-001");

        boolean result = invoiceRepository.existsByInvoiceIdAndTenantId(
            invoice.getInvoiceId(), TENANT_ID);

        assertTrue(result);
    }

    // DELETE Tests
    @Test
    void testDeleteById_Success() {
        Invoice invoice = createAndSaveInvoice("INV-001");

        invoiceRepository.deleteById(invoice.getId());

        Optional<Invoice> result = invoiceRepository.findById(invoice.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteByInvoiceIdAndTenantId_Success() {
        Invoice invoice = createAndSaveInvoice("INV-001");

        invoiceRepository.deleteByInvoiceIdAndTenantId(invoice.getInvoiceId(), TENANT_ID);

        Optional<Invoice> result = invoiceRepository.findByInvoiceIdAndTenantId(
            invoice.getInvoiceId(), TENANT_ID);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteAllByTenantId_Success() {
        createAndSaveInvoice("INV-001");
        createAndSaveInvoice("INV-002");
        createAndSaveInvoice("INV-003");

        invoiceRepository.deleteAllByTenantId(TENANT_ID);

        List<Invoice> result = invoiceRepository.findByTenantId(TENANT_ID);
        assertEquals(0, result.size());
    }

    // COUNT Tests
    @Test
    void testCountByTenantId_Success() {
        createAndSaveInvoice("INV-001");
        createAndSaveInvoice("INV-002");
        createAndSaveInvoice("INV-003");

        long count = invoiceRepository.countByTenantId(TENANT_ID);

        assertEquals(3, count);
    }

    @Test
    void testCountByTenantIdAndStatus_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        Invoice inv3 = createAndSaveInvoice("INV-003");
        inv1.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv2.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv3.setStatus(Invoice.InvoiceStatus.PENDING);
        invoiceRepository.saveAll(List.of(inv1, inv2, inv3));

        long count = invoiceRepository.countByTenantIdAndStatus(
            TENANT_ID, Invoice.InvoiceStatus.APPROVED);

        assertEquals(2, count);
    }

    // SUM AMOUNT Tests
    @Test
    void testSumAmountByTenantIdAndStatus_Success() {
        Invoice inv1 = createAndSaveInvoice("INV-001");
        Invoice inv2 = createAndSaveInvoice("INV-002");
        inv1.setAmount(new BigDecimal("1000.00"));
        inv2.setAmount(new BigDecimal("2000.00"));
        inv1.setStatus(Invoice.InvoiceStatus.APPROVED);
        inv2.setStatus(Invoice.InvoiceStatus.APPROVED);
        invoiceRepository.saveAll(List.of(inv1, inv2));

        BigDecimal sum = invoiceRepository.sumAmountByTenantIdAndStatus(
            TENANT_ID, Invoice.InvoiceStatus.APPROVED);

        assertEquals(new BigDecimal("3000.00"), sum);
    }

    // Helper methods
    private Invoice createTestInvoice(String invoiceNumber) {
        return Invoice.builder()
            .tenantId(TENANT_ID)
            .vendorId(VENDOR_ID)
            .vendorName(VENDOR_NAME)
            .invoiceNumber(invoiceNumber)
            .invoiceDate(LocalDate.now())
            .dueDate(LocalDate.now().plusDays(30))
            .amount(new BigDecimal("1000.00"))
            .currency("USD")
            .status(Invoice.InvoiceStatus.DRAFT)
            .lineItems(new ArrayList<>())
            .attachments(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();
    }

    private Invoice createAndSaveInvoice(String invoiceNumber) {
        return invoiceRepository.save(createTestInvoice(invoiceNumber));
    }

    private Invoice createAndSaveInvoiceWithVendor(String invoiceNumber, String vendorId) {
        Invoice invoice = createTestInvoice(invoiceNumber);
        invoice.setVendorId(vendorId);
        return invoiceRepository.save(invoice);
    }

    private void setTenantContext(String tenantId) {
        RequestContext context = RequestContext.builder()
            .tenantId(tenantId)
            .userId("test-user")
            .build();
        RequestContextHolder.setContext(context);
    }
}
