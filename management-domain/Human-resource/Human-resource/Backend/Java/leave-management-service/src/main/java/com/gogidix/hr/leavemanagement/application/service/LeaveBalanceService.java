package com.gogidix.hr.leavemanagement.application.service;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.event.LeaveBalanceUpdatedEvent;
import com.gogidix.hr.leavemanagement.domain.model.LeaveBalance;
import com.gogidix.hr.leavemanagement.domain.model.LeavePolicy;
import com.gogidix.hr.leavemanagement.domain.port.out.EventPublisher;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveBalanceRepository;
import com.gogidix.hr.leavemanagement.domain.repository.LeavePolicyRepository;
import com.gogidix.hr.leavemanagement.shared.exception.LeaveNotFoundException;
import com.gogidix.hr.leavemanagement.shared.exception.LeaveValidationException;
import com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Leave Balance Service
 * Handles leave balance operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EventPublisher eventPublisher;

    public LeaveBalance getByEmployeeAndType(String employeeId, LeaveType leaveType) {
        String year = String.valueOf(LocalDate.now().getYear());
        return leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance not found for employee: " + employeeId + " and type: " + leaveType));
    }

    public List<LeaveBalance> getByEmployee(String employeeId) {
        return leaveBalanceRepository.findAllByEmployeeId(employeeId);
    }

    public List<LeaveBalance> getByEmployeeAndYear(String employeeId, String year) {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveBalanceRepository.findByTenantIdAndEmployeeIdAndYear(tenantId, employeeId, year);
    }

    public List<LeaveBalance> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveBalanceRepository.findByTenantId(tenantId);
    }

    public List<LeaveBalance> getLowBalances() {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveBalanceRepository.findLowBalances(tenantId);
    }

    public List<LeaveBalance> getExpiringCarryForward() {
        String tenantId = RequestContextHolder.getTenantId();
        return leaveBalanceRepository.findExpiringCarryForward(tenantId);
    }

    @Transactional
    public LeaveBalance deductBalance(String balanceId, Double days) {
        log.info("Deducting {} days from balance: {}", days, balanceId);

        LeaveBalance balance = leaveBalanceRepository.findById(balanceId)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", balanceId));

        if (!balance.hasSufficientBalance(days)) {
            throw new LeaveValidationException("Insufficient balance. Available: " + balance.getAvailable());
        }

        Double previousBalance = balance.getAvailable();
        balance.deductBalance(days);
        LeaveBalance savedBalance = leaveBalanceRepository.save(balance);

        // Publish event
        eventPublisher.publish("leave-balance-updated", new LeaveBalanceUpdatedEvent(
                savedBalance.getTenantId(),
                savedBalance.getBalanceId(),
                savedBalance.getEmployeeId(),
                savedBalance.getLeaveType().name(),
                previousBalance,
                savedBalance.getAvailable(),
                savedBalance.getUsed(),
                0.0,
                "DEDUCT",
                LocalDate.now().toString()
        ));

        return savedBalance;
    }

    @Transactional
    public LeaveBalance addBalance(String balanceId, Double days) {
        log.info("Adding {} days to balance: {}", days, balanceId);

        LeaveBalance balance = leaveBalanceRepository.findById(balanceId)
                .orElseThrow(() -> new LeaveNotFoundException("LeaveBalance", balanceId));

        Double previousBalance = balance.getAvailable();
        balance.addBalance(days);
        LeaveBalance savedBalance = leaveBalanceRepository.save(balance);

        // Publish event
        eventPublisher.publish(new LeaveBalanceUpdatedEvent(
                    savedBalance.getTenantId(),
                    savedBalance.getBalanceId(),
                    savedBalance.getEmployeeId(),
                    savedBalance.getLeaveType().name(),
                    previousBalance,
                    savedBalance.getAvailable(),
                    0.0,
                    days,
                    "ADD",
                    LocalDate.now().toString()
            ));

        return savedBalance;
    }

    @Transactional
    public void processYearEnd(String year) {
        log.info("Processing year-end for: {}", year);

        String tenantId = RequestContextHolder.getTenantId();
        List<LeaveBalance> balances = leaveBalanceRepository.findByTenantIdAndYear(tenantId, year);

        for (LeaveBalance balance : balances) {
            // Forfeit expired carry forward
            balance.forfeitExpiredCarryForward();

            // Process based on policy
            LeavePolicy policy = balance.getPolicyId() != null ?
                    leavePolicyRepository.findById(balance.getPolicyId()).orElse(null) : null;

            if (policy != null && policy.getYearEndProcessing() != null) {
                switch (policy.getYearEndProcessing()) {
                    case LAPSE:
                        if (balance.getAvailable() > 0) {
                            balance.setForfeited((balance.getForfeited() != null ? balance.getForfeited() : 0.0) + balance.getAvailable());
                            balance.setAvailable(0.0);
                        }
                        break;
                    case CARRY_FORWARD:
                        if (balance.getCarryForwardLimit() != null) {
                            LocalDate expiryDate = LocalDate.now().plusDays(
                                    policy.getCarryForwardExpiryDays() != null ? policy.getCarryForwardExpiryDays() : 90
                            );
                            balance.carryForward(balance.getAvailable(), expiryDate);
                        }
                        break;
                    case ROLL_OVER:
                        // Default - keep available
                        break;
                    default:
                        break;
                }
            }

            leaveBalanceRepository.save(balance);
        }

        log.info("Completed year-end processing for: {}", year);
    }
}
