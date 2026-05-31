package com.gogidix.finance.accountspayable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.repository.PaymentRepository;
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
 * MongoDB Repository Implementation - Payment
 * Implements payment persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPaymentRepository implements PaymentRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Payment save(Payment payment) {
        log.debug("Saving payment: {} for tenant: {}",
            payment.getPaymentId(), payment.getTenantId());
        return mongoTemplate.save(payment);
    }

    @Override
    public List<Payment> saveAll(List<Payment> payments) {
        return payments.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<Payment> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payment.class));
    }

    @Override
    public Optional<Payment> findByPaymentIdAndTenantId(String paymentId, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentId").is(paymentId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payment.class));
    }

    @Override
    public Optional<Payment> findByPaymentReferenceAndTenantId(String paymentReference, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentReference").is(paymentReference)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Payment.class));
    }

    @Override
    public List<Payment> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndVendorId(String tenantId, String vendorId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("vendorId").is(vendorId)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndInvoiceIdsContaining(String tenantId, String invoiceId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceIds").in(invoiceId)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndPaymentMethod(String tenantId, Payment.PaymentMethod paymentMethod) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("paymentMethod").is(paymentMethod)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate,
                                                              LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("paymentDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndScheduledDateBetween(String tenantId, LocalDate startDate,
                                                                LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("scheduledDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndStatusIn(String tenantId, List<Payment.PaymentStatus> statuses) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(statuses)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndProcessedBy(String tenantId, String processedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("processedBy").is(processedBy)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndBatchId(String tenantId, String batchId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("batchId").is(batchId)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findScheduledPayments(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Payment.PaymentStatus.SCHEDULED)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findPendingPayments(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Payment.PaymentStatus.PENDING)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findFailedPayments(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Payment.PaymentStatus.FAILED)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> searchByDescription(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .orOperator(
                    Criteria.where("description").regex(searchTerm, "i"),
                    Criteria.where("paymentReference").regex(searchTerm, "i"),
                    Criteria.where("transactionReference").regex(searchTerm, "i")
                )
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndAmountBetween(String tenantId, BigDecimal minAmount,
                                                         BigDecimal maxAmount) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("amount").gte(minAmount).lte(maxAmount)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public boolean existsByPaymentIdAndTenantId(String paymentId, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentId").is(paymentId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Payment.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Payment.class);
    }

    @Override
    public void deleteByPaymentIdAndTenantId(String paymentId, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentId").is(paymentId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Payment.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Payment.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Payment.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Payment.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );

        List<Payment> payments = mongoTemplate.find(query, Payment.class);
        return payments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
