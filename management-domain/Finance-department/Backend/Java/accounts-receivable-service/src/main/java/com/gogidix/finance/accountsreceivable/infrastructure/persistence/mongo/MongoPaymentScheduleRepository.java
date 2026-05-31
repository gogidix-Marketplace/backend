package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountsreceivable.domain.model.PaymentSchedule;
import com.gogidix.finance.accountsreceivable.domain.repository.PaymentScheduleRepository;
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
 * MongoDB Repository Implementation - PaymentSchedule
 * Implements payment schedule persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPaymentScheduleRepository implements PaymentScheduleRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PaymentSchedule save(PaymentSchedule paymentSchedule) {
        log.debug("Saving payment schedule: {} for tenant: {}",
            paymentSchedule.getScheduleId(), paymentSchedule.getTenantId());
        return mongoTemplate.save(paymentSchedule);
    }

    @Override
    public List<PaymentSchedule> saveAll(List<PaymentSchedule> paymentSchedules) {
        return paymentSchedules.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<PaymentSchedule> findById(String id) {
        String tenantId = com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PaymentSchedule.class));
    }

    @Override
    public Optional<PaymentSchedule> findByScheduleIdAndTenantId(String scheduleId, String tenantId) {
        Query query = Query.query(
            Criteria.where("scheduleId").is(scheduleId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PaymentSchedule.class));
    }

    @Override
    public Optional<PaymentSchedule> findByScheduleNumberAndTenantId(String scheduleNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("scheduleNumber").is(scheduleNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PaymentSchedule.class));
    }

    @Override
    public List<PaymentSchedule> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndInvoiceId(String tenantId, String invoiceId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("invoiceId").is(invoiceId)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndStatus(String tenantId, PaymentSchedule.ScheduleStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndScheduleType(String tenantId, PaymentSchedule.ScheduleType scheduleType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("scheduleType").is(scheduleType)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findActiveSchedulesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findPendingPaymentsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
                .and("scheduledPayments.status").is(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findPendingPaymentsByTenantIdAndDueDate(String tenantId, LocalDate dueDate) {
        Date date = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
                .and("scheduledPayments.dueDate").is(date)
                .and("scheduledPayments.status").is(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findPendingPaymentsByTenantIdAndDueDateBefore(String tenantId, LocalDate dueDate) {
        Date date = Date.from(dueDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
                .and("scheduledPayments.dueDate").lte(date)
                .and("scheduledPayments.status").is(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndProjectId(String tenantId, String projectId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("projectId").is(projectId)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndDepartmentId(String tenantId, String departmentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("departmentId").is(departmentId)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findDefaultedSchedulesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.DEFAULTED)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public List<PaymentSchedule> findByTenantIdAndAutoChargeTrue(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("autoCharge").is(true)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }

    @Override
    public boolean existsByScheduleNumberAndTenantId(String scheduleNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("scheduleNumber").is(scheduleNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, PaymentSchedule.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), PaymentSchedule.class);
    }

    @Override
    public void deleteByScheduleIdAndTenantId(String scheduleId, String tenantId) {
        Query query = Query.query(
            Criteria.where("scheduleId").is(scheduleId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, PaymentSchedule.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, PaymentSchedule.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, PaymentSchedule.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, PaymentSchedule.ScheduleStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, PaymentSchedule.class);
    }

    @Override
    public BigDecimal sumBalanceRemainingByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<PaymentSchedule> schedules = mongoTemplate.find(query, PaymentSchedule.class);
        return schedules.stream()
            .map(PaymentSchedule::getBalanceRemaining)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumBalanceRemainingByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        List<PaymentSchedule> schedules = mongoTemplate.find(query, PaymentSchedule.class);
        return schedules.stream()
            .map(PaymentSchedule::getBalanceRemaining)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<PaymentSchedule> findSchedulesRequiringReminderByTenantIdAndDueDate(String tenantId, LocalDate dueDate, Integer reminderDaysBefore) {
        LocalDate reminderDate = dueDate.minusDays(reminderDaysBefore);
        Date date = Date.from(reminderDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(PaymentSchedule.ScheduleStatus.ACTIVE)
                .and("sendReminder").is(true)
                .and("scheduledPayments.dueDate").is(date)
                .and("scheduledPayments.status").is(PaymentSchedule.ScheduledPayment.PaymentStatus.PENDING)
        );
        return mongoTemplate.find(query, PaymentSchedule.class);
    }
}
