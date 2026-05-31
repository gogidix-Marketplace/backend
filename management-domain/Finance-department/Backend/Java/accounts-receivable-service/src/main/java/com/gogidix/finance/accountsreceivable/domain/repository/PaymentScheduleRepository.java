package com.gogidix.finance.accountsreceivable.domain.repository;

import com.gogidix.finance.accountsreceivable.domain.model.PaymentSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Payment Schedule Repository Interface (Port)
 * Defines the contract for payment schedule persistence operations
 */
public interface PaymentScheduleRepository {

    PaymentSchedule save(PaymentSchedule paymentSchedule);

    List<PaymentSchedule> saveAll(List<PaymentSchedule> paymentSchedules);

    Optional<PaymentSchedule> findById(String id);

    Optional<PaymentSchedule> findByScheduleIdAndTenantId(String scheduleId, String tenantId);

    Optional<PaymentSchedule> findByScheduleNumberAndTenantId(String scheduleNumber, String tenantId);

    List<PaymentSchedule> findByTenantId(String tenantId);

    List<PaymentSchedule> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<PaymentSchedule> findByTenantIdAndInvoiceId(String tenantId, String invoiceId);

    List<PaymentSchedule> findByTenantIdAndStatus(String tenantId, PaymentSchedule.ScheduleStatus status);

    List<PaymentSchedule> findByTenantIdAndScheduleType(String tenantId, PaymentSchedule.ScheduleType scheduleType);

    List<PaymentSchedule> findActiveSchedulesByTenantId(String tenantId);

    List<PaymentSchedule> findPendingPaymentsByTenantId(String tenantId);

    List<PaymentSchedule> findPendingPaymentsByTenantIdAndDueDate(String tenantId, LocalDate dueDate);

    List<PaymentSchedule> findPendingPaymentsByTenantIdAndDueDateBefore(String tenantId, LocalDate dueDate);

    List<PaymentSchedule> findByTenantIdAndProjectId(String tenantId, String projectId);

    List<PaymentSchedule> findByTenantIdAndDepartmentId(String tenantId, String departmentId);

    List<PaymentSchedule> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<PaymentSchedule> findDefaultedSchedulesByTenantId(String tenantId);

    List<PaymentSchedule> findByTenantIdAndAutoChargeTrue(String tenantId);

    boolean existsByScheduleNumberAndTenantId(String scheduleNumber, String tenantId);

    void deleteById(String id);

    void deleteByScheduleIdAndTenantId(String scheduleId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, PaymentSchedule.ScheduleStatus status);

    BigDecimal sumBalanceRemainingByTenantId(String tenantId);

    BigDecimal sumBalanceRemainingByTenantIdAndCustomerId(String tenantId, String customerId);

    List<PaymentSchedule> findSchedulesRequiringReminderByTenantIdAndDueDate(String tenantId, LocalDate dueDate, Integer reminderDaysBefore);
}
