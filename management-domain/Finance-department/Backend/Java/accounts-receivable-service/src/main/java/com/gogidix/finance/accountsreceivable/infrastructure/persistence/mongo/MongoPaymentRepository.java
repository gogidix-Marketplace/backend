package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.repository.PaymentRepository;
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
        String tenantId = com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder.getTenantId();
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
    public Optional<Payment> findByPaymentNumberAndTenantId(String paymentNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentNumber").is(paymentNumber)
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
    public List<Payment> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndInvoiceId(String tenantId, String invoiceId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceId").is(invoiceId)
        );
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
    public List<Payment> findByTenantIdAndStatusIn(String tenantId, List<Payment.PaymentStatus> statuses) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(statuses)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndPaymentType(String tenantId, Payment.PaymentType paymentType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("paymentType").is(paymentType)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndPaymentMethod(String tenantId, String paymentMethod) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("paymentMethod").is(paymentMethod)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("paymentDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findUnallocatedPaymentsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("unappliedAmount").gt(BigDecimal.ZERO)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findReconciledPaymentsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("reconciled").is(true)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findUnreconciledPaymentsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("reconciled").is(false)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndCustomerIdAndStatus(String tenantId, String customerId, Payment.PaymentStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndReferenceNumber(String tenantId, String referenceNumber) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("referenceNumber").is(referenceNumber)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndCheckNumber(String tenantId, String checkNumber) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("checkNumber").is(checkNumber)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public List<Payment> findByTenantIdAndBankAccount(String tenantId, String bankAccount) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("bankAccount").is(bankAccount)
        );
        return mongoTemplate.find(query, Payment.class);
    }

    @Override
    public boolean existsByPaymentNumberAndTenantId(String paymentNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("paymentNumber").is(paymentNumber)
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

    @Override
    public BigDecimal sumAmountByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        List<Payment> payments = mongoTemplate.find(query, Payment.class);
        return payments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumUnappliedAmountByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<Payment> payments = mongoTemplate.find(query, Payment.class);
        return payments.stream()
            .map(Payment::getUnappliedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Payment> findPaymentsByTransactionId(String tenantId, String transactionId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("transactionId").is(transactionId)
        );
        return mongoTemplate.find(query, Payment.class);
    }
}
