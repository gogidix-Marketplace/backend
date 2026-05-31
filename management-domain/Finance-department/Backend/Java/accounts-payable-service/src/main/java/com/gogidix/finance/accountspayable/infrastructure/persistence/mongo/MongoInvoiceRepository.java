package com.gogidix.finance.accountspayable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Invoice
 * Implements invoice persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoInvoiceRepository implements InvoiceRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Invoice save(Invoice invoice) {
        log.debug("Saving invoice: {} for tenant: {}",
            invoice.getInvoiceId(), invoice.getTenantId());
        return mongoTemplate.save(invoice);
    }

    @Override
    public List<Invoice> saveAll(List<Invoice> invoices) {
        return invoices.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<Invoice> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Invoice.class));
    }

    @Override
    public Optional<Invoice> findByInvoiceIdAndTenantId(String invoiceId, String tenantId) {
        Query query = Query.query(
            Criteria.where("invoiceId").is(invoiceId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Invoice.class));
    }

    @Override
    public Optional<Invoice> findByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("invoiceNumber").is(invoiceNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Invoice.class));
    }

    @Override
    public List<Invoice> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndVendorId(String tenantId, String vendorId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("vendorId").is(vendorId)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("department").is(department)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("costCenter").is(costCenter)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndInvoiceDateBetween(String tenantId, LocalDate startDate,
                                                              LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate,
                                                          LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndStatusIn(String tenantId, List<Invoice.InvoiceStatus> statuses) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(statuses)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndApprovedBy(String tenantId, String approvedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("approvedBy").is(approvedBy)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndSubmittedBy(String tenantId, String submittedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("submittedBy").is(submittedBy)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findOverdueInvoices(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").lt(today)
                .and("status").in(List.of(
                    Invoice.InvoiceStatus.PENDING,
                    Invoice.InvoiceStatus.APPROVED,
                    Invoice.InvoiceStatus.OVERDUE
                ))
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findInvoicesDueForPayment(String tenantId, LocalDate dueDate) {
        LocalDate targetDate = dueDate != null ? dueDate : LocalDate.now();
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").lte(targetDate)
                .and("status").in(List.of(
                    Invoice.InvoiceStatus.APPROVED,
                    Invoice.InvoiceStatus.PENDING
                ))
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> searchByDescription(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .orOperator(
                    Criteria.where("description").regex(searchTerm, "i"),
                    Criteria.where("invoiceNumber").regex(searchTerm, "i"),
                    Criteria.where("internalReference").regex(searchTerm, "i")
                )
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndAmountBetween(String tenantId, BigDecimal minAmount,
                                                         BigDecimal maxAmount) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("amount").gte(minAmount).lte(maxAmount)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public boolean existsByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("invoiceNumber").is(invoiceNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Invoice.class);
    }

    @Override
    public boolean existsByInvoiceIdAndTenantId(String invoiceId, String tenantId) {
        Query query = Query.query(
            Criteria.where("invoiceId").is(invoiceId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Invoice.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Invoice.class);
    }

    @Override
    public void deleteByInvoiceIdAndTenantId(String invoiceId, String tenantId) {
        Query query = Query.query(
            Criteria.where("invoiceId").is(invoiceId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Invoice.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Invoice.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Invoice.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Invoice.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );

        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        return invoices.stream()
            .map(Invoice::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
