package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.repository.InvoiceRepository;
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
        String tenantId = com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder.getTenantId();
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
    public List<Invoice> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
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
    public List<Invoice> findByTenantIdAndStatusIn(String tenantId, List<Invoice.InvoiceStatus> statuses) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(statuses)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndInvoiceType(String tenantId, Invoice.InvoiceType invoiceType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceType").is(invoiceType)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndInvoiceDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("dueDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findOverdueInvoicesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Invoice.InvoiceStatus.OVERDUE)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findPendingInvoicesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(List.of(Invoice.InvoiceStatus.SENT, Invoice.InvoiceStatus.VIEWED))
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndSalesperson(String tenantId, String salesperson) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesperson").is(salesperson)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndProjectId(String tenantId, String projectId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("projectId").is(projectId)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndDepartmentId(String tenantId, String departmentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("departmentId").is(departmentId)
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
    public List<Invoice> findByTenantIdAndCustomerNameContainingIgnoreCase(String tenantId, String customerName) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerName").regex(customerName, "i")
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndPurchaseOrderNumber(String tenantId, String purchaseOrderNumber) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("purchaseOrderNumber").is(purchaseOrderNumber)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndGroupId(String tenantId, String groupId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("groupId").is(groupId)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findByTenantIdAndParentId(String tenantId, String parentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentId").is(parentId)
        );
        return mongoTemplate.find(query, Invoice.class);
    }

    @Override
    public List<Invoice> findRecurringInvoicesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isRecurring").is(true)
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
    public BigDecimal sumTotalAmountByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        return invoices.stream()
            .map(Invoice::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumBalanceDueByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        return invoices.stream()
            .map(Invoice::getBalanceDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumBalanceDueByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        return invoices.stream()
            .map(Invoice::getBalanceDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Invoice> findByTenantIdAndDaysOverdueGreaterThan(String tenantId, Integer daysOverdue) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("daysOverdue").gt(daysOverdue)
        );
        return mongoTemplate.find(query, Invoice.class);
    }
}
