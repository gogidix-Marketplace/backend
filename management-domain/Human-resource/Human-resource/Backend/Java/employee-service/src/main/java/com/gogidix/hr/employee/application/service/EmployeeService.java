package com.gogidix.hr.employee.application.service;

import com.gogidix.hr.employee.domain.event.DomainEvent;
import com.gogidix.hr.employee.domain.event.EmployeeCreatedEvent;
import com.gogidix.hr.employee.domain.event.EmployeeTerminatedEvent;
import com.gogidix.hr.employee.domain.model.Employee;
import com.gogidix.hr.employee.domain.model.EmploymentRecord;
import com.gogidix.hr.employee.domain.repository.EmployeeRepository;
import com.gogidix.hr.employee.domain.repository.EmploymentRecordRepository;
import com.gogidix.hr.employee.shared.exception.ConflictException;
import com.gogidix.hr.employee.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Employee Application Service
 * Orchestrates business operations for employee management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRecordRepository employmentRecordRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String EMPLOYEE_CREATED_TOPIC = "employee-created";
    private static final String EMPLOYEE_TERMINATED_TOPIC = "employee-terminated";
    private static final String EMPLOYEE_UPDATED_TOPIC = "employee-updated";

    /**
     * Creates a new employee
     */
    @Transactional
    public Employee createEmployee(String firstName, String lastName, String email,
                                   String department, String position,
                                   Employee.EmployeeLevel level,
                                   Employee.EmploymentType employmentType,
                                   LocalDate hireDate, String createdBy) {
        // Validate email uniqueness
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Employee with email " + email + " already exists");
        }

        // Get tenant ID from request context
        String tenantId = getTenantId();

        // Create employee
        Employee employee = Employee.create(
            tenantId, firstName, lastName, email, department,
            position, level, employmentType, hireDate, createdBy
        );

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Create initial employment record
        EmploymentRecord record = EmploymentRecord.createInitialRecord(
            savedEmployee.getId(), tenantId, department, position, level, hireDate
        );
        employmentRecordRepository.save(record);

        // Publish domain events
        publishDomainEvents(savedEmployee);

        log.info("Created employee: {} for tenant: {}", savedEmployee.getEmployeeNumber(), tenantId);
        return savedEmployee;
    }

    /**
     * Gets employee by ID
     */
    @Cacheable(value = "employees", key = "#id")
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    /**
     * Gets employee by employee number
     */
    @Cacheable(value = "employees", key = "#employeeNumber")
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber);
    }

    /**
     * Gets employee by email
     */
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    /**
     * Searches employees with pagination
     */
    @Transactional(readOnly = true)
    public Page<Employee> searchEmployees(String searchTerm, Pageable pageable) {
        String tenantId = getTenantId();
        if (searchTerm == null || searchTerm.isBlank()) {
            return employeeRepository.findByTenantId(tenantId, pageable);
        }
        return employeeRepository.searchByTenantIdAndSearchTerm(tenantId, searchTerm, pageable);
    }

    /**
     * Gets all active employees for tenant
     */
    @Transactional(readOnly = true)
    public List<Employee> getActiveEmployees() {
        String tenantId = getTenantId();
        return employeeRepository.findByTenantIdAndStatus(tenantId, Employee.EmployeeStatus.ACTIVE);
    }

    /**
     * Gets employees by department
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(String departmentId) {
        String tenantId = getTenantId();
        return employeeRepository.findByTenantIdAndDepartmentId(tenantId, departmentId);
    }

    /**
     * Gets employees by manager
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByManager(String managerId) {
        String tenantId = getTenantId();
        return employeeRepository.findByTenantIdAndManagerId(tenantId, managerId);
    }

    /**
     * Activates an employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee activateEmployee(String id) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.activate();
        Employee savedEmployee = employeeRepository.save(employee);

        publishEmployeeUpdated(savedEmployee, "ACTIVATED");
        log.info("Activated employee: {}", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Deactivates an employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee deactivateEmployee(String id) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.deactivate();
        Employee savedEmployee = employeeRepository.save(employee);

        publishEmployeeUpdated(savedEmployee, "DEACTIVATED");
        log.info("Deactivated employee: {}", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Terminates an employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee terminateEmployee(String id, String reason, String category,
                                     LocalDate lastWorkingDay) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.terminate(reason, category, lastWorkingDay);
        Employee savedEmployee = employeeRepository.save(employee);

        // Publish termination events
        publishDomainEvents(savedEmployee);

        log.info("Terminated employee: {} for reason: {}", employee.getEmployeeNumber(), reason);
        return savedEmployee;
    }

    /**
     * Processes employee resignation
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee resignEmployee(String id, String reason, LocalDate lastWorkingDay,
                                  String rehireEligibility) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.resign(reason, lastWorkingDay, rehireEligibility);
        Employee savedEmployee = employeeRepository.save(employee);

        // Publish resignation events
        publishDomainEvents(savedEmployee);

        log.info("Processed resignation for employee: {}", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Promotes an employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee promoteEmployee(String id, Employee.EmployeeLevel newLevel,
                                   String newPosition, Double newSalary) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.promote(newLevel, newPosition, newSalary);
        Employee savedEmployee = employeeRepository.save(employee);

        publishEmployeeUpdated(savedEmployee, "PROMOTED");
        log.info("Promoted employee: {} to level: {}", employee.getEmployeeNumber(), newLevel);
        return savedEmployee;
    }

    /**
     * Transfers an employee to new department
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee transferEmployee(String id, String newDepartmentId, String newDepartment,
                                    String newPosition, String newManagerId) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.transfer(newDepartmentId, newDepartment, newPosition, newManagerId);
        Employee savedEmployee = employeeRepository.save(employee);

        publishEmployeeUpdated(savedEmployee, "TRANSFERRED");
        log.info("Transferred employee: {} to department: {}", employee.getEmployeeNumber(), newDepartment);
        return savedEmployee;
    }

    /**
     * Updates employee salary
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee updateSalary(String id, Double newSalary, String reason) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.updateSalary(newSalary, reason);
        Employee savedEmployee = employeeRepository.save(employee);

        publishEmployeeUpdated(savedEmployee, "SALARY_UPDATED");
        log.info("Updated salary for employee: {}", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Updates employee personal information
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee updatePersonalInfo(String id, String firstName, String lastName,
                                      String email, String phone, String mobile) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        // Check if email is being changed and if new email already exists
        if (!employee.getEmail().equals(email) &&
            employeeRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Employee with email " + email + " already exists");
        }

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setMobile(mobile);

        Employee savedEmployee = employeeRepository.save(employee);
        publishEmployeeUpdated(savedEmployee, "PERSONAL_INFO_UPDATED");
        return savedEmployee;
    }

    /**
     * Puts employee on leave
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee putOnLeave(String id, String leaveType, LocalDate startDate,
                              LocalDate expectedReturnDate) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.onLeave();
        employee.setCustomField("leaveType", leaveType);
        employee.setCustomField("leaveStartDate", startDate);
        employee.setCustomField("expectedReturnDate", expectedReturnDate);

        Employee savedEmployee = employeeRepository.save(employee);
        publishEmployeeUpdated(savedEmployee, "ON_LEAVE");
        log.info("Put employee: {} on leave", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Returns employee from leave
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee returnFromLeave(String id) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.returnFromLeave();
        employee.setCustomField("leaveType", null);
        employee.setCustomField("leaveStartDate", null);
        employee.setCustomField("expectedReturnDate", null);

        Employee savedEmployee = employeeRepository.save(employee);
        publishEmployeeUpdated(savedEmployee, "RETURNED_FROM_LEAVE");
        log.info("Employee: {} returned from leave", employee.getEmployeeNumber());
        return savedEmployee;
    }

    /**
     * Adds skill to employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee addSkill(String id, String skill) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        if (skill == null || skill.isBlank()) {
            throw new ValidationException("skill", "Skill cannot be empty");
        }

        employee.addSkill(skill);
        return employeeRepository.save(employee);
    }

    /**
     * Removes skill from employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee removeSkill(String id, String skill) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.removeSkill(skill);
        return employeeRepository.save(employee);
    }

    /**
     * Adds certification to employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee addCertification(String id, String certification) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        if (certification == null || certification.isBlank()) {
            throw new ValidationException("certification", "Certification cannot be empty");
        }

        employee.addCertification(certification);
        return employeeRepository.save(employee);
    }

    /**
     * Removes certification from employee
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public Employee removeCertification(String id, String certification) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        employee.removeCertification(certification);
        return employeeRepository.save(employee);
    }

    /**
     * Adds direct report to manager
     */
    @CacheEvict(value = "employees", key = "#managerId")
    @Transactional
    public Employee addDirectReport(String managerId, String employeeId) {
        Employee manager = getEmployeeByIdOrThrow(managerId);
        validateTenantAccess(manager);

        if (!manager.canManageOthers()) {
            throw new IllegalStateException("Employee " + manager.getEmployeeNumber() +
                " is not eligible to manage others");
        }

        manager.addDirectReport(employeeId);
        return employeeRepository.save(manager);
    }

    /**
     * Removes direct report from manager
     */
    @CacheEvict(value = "employees", key = "#managerId")
    @Transactional
    public Employee removeDirectReport(String managerId, String employeeId) {
        Employee manager = getEmployeeByIdOrThrow(managerId);
        validateTenantAccess(manager);

        manager.removeDirectReport(employeeId);
        return employeeRepository.save(manager);
    }

    /**
     * Deletes an employee (soft delete by setting active to false)
     */
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public void deleteEmployee(String id) {
        Employee employee = getEmployeeByIdOrThrow(id);
        validateTenantAccess(employee);

        if (employee.getStatus() == Employee.EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Cannot delete active employee. Deactivate first.");
        }

        employeeRepository.deleteById(id);
        log.info("Deleted employee: {}", employee.getEmployeeNumber());
    }

    /**
     * Gets employee count by status for tenant
     */
    @Transactional(readOnly = true)
    public long getEmployeeCountByStatus(Employee.EmployeeStatus status) {
        String tenantId = getTenantId();
        return employeeRepository.countByTenantIdAndStatus(tenantId, status);
    }

    /**
     * Gets employee count by department
     */
    @Transactional(readOnly = true)
    public long getEmployeeCountByDepartment(String departmentId) {
        String tenantId = getTenantId();
        return employeeRepository.countByTenantIdAndDepartmentId(tenantId, departmentId);
    }

    // Helper methods

    private Employee getEmployeeByIdOrThrow(String id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new ValidationException("id", "Employee not found with id: " + id));
    }

    private void validateTenantAccess(Employee employee) {
        String tenantId = getTenantId();
        if (!employee.getTenantId().equals(tenantId)) {
            throw new IllegalStateException("Employee does not belong to current tenant");
        }
    }

    private String getTenantId() {
        // This would get tenant ID from request context/JWT
        // For now, return a default
        return "tenant-123";
    }

    private void publishDomainEvents(Employee employee) {
        List<DomainEvent> events = employee.getDomainEvents();
        for (Object event : events) {
            if (event instanceof EmployeeTerminatedEvent) {
                kafkaTemplate.send(EMPLOYEE_TERMINATED_TOPIC, event);
            } else if (event instanceof EmployeeCreatedEvent) {
                kafkaTemplate.send(EMPLOYEE_CREATED_TOPIC, event);
            }
        }
        employee.clearDomainEvents();
    }

    private void publishEmployeeUpdated(Employee employee, String action) {
        kafkaTemplate.send(EMPLOYEE_UPDATED_TOPIC, action);
    }
}
