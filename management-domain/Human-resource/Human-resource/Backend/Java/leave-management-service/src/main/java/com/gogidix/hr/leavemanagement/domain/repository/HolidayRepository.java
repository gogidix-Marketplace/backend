package com.gogidix.hr.leavemanagement.domain.repository;

import com.gogidix.hr.leavemanagement.domain.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Holiday Repository Interface
 */
public interface HolidayRepository {

    Holiday save(Holiday holiday);

    List<Holiday> saveAll(List<Holiday> holidays);

    Optional<Holiday> findById(String id);

    Optional<Holiday> findByHolidayIdAndTenantId(String holidayId, String tenantId);

    List<Holiday> findByTenantId(String tenantId);

    List<Holiday> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<Holiday> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<Holiday> findByTenantIdAndHolidayDate(String tenantId, LocalDate date);

    List<Holiday> findByTenantIdAndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Holiday> findByTenantIdAndCountryCodeAndDateBetween(String tenantId, String countryCode, LocalDate startDate, LocalDate endDate);

    List<Holiday> findRecurringHolidays(String tenantId);

    List<Holiday> findUpcomingHolidays(String tenantId, LocalDate fromDate, int limit);

    boolean existsHolidayOnDate(String tenantId, LocalDate date);

    void deleteById(String id);

    void deleteByHolidayIdAndTenantId(String holidayId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    List<Holiday> findByTenantIdAndCategory(String tenantId, String category);
}