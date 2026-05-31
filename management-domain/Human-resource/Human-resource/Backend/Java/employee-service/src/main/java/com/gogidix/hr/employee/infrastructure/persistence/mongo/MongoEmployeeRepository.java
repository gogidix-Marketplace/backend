package com.gogidix.hr.employee.infrastructure.persistence.mongo;

import com.gogidix.hr.employee.domain.model.Employee;
import com.gogidix.hr.employee.domain.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Employee
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoEmployeeRepository implements EmployeeRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Employee save(Employee employee) {
        return mongoTemplate.save(employee);
    }

    @Override
    public List<Employee> saveAll(List<Employee> employees) {
        Collection<Employee> saved = mongoTemplate.insertAll(employees);
        return saved.stream().toList();
    }

    @Override
    public Optional<Employee> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Employee.class));
    }

    @Override
    public Optional<Employee> findByEmployeeNumberAndTenantId(String employeeNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("employeeNumber").is(employeeNumber).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Employee.class));
    }

    @Override
    public Optional<Employee> findByEmailAndTenantId(String email, String tenantId) {
        Query query = Query.query(
            Criteria.where("email").is(email).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Employee.class));
    }

    public Optional<Employee> findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, Employee.class));
    }

    @Override
    public Optional<Employee> findByEmployeeNumber(String employeeNumber) {
        Query query = Query.query(Criteria.where("employeeNumber").is(employeeNumber));
        return Optional.ofNullable(mongoTemplate.findOne(query, Employee.class));
    }

    @Override
    public List<Employee> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndStatus(String tenantId, Employee.EmployeeStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("department").is(department)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndDepartmentId(String tenantId, String departmentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("departmentId").is(departmentId)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndManagerId(String tenantId, String managerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("managerId").is(managerId)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndLevel(String tenantId, Employee.EmployeeLevel level) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("level").is(level)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndEmploymentType(String tenantId, Employee.EmploymentType employmentType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("employmentType").is(employmentType)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndLocation(String tenantId, String location) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("location").is(location)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("costCenter").is(costCenter)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndPositionId(String tenantId, String positionId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("positionId").is(positionId)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndHireDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("hireDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndTerminationDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("terminationDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findActiveEmployees(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(Employee.EmployeeStatus.ACTIVE)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findInactiveEmployees(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(Employee.EmployeeStatus.INACTIVE)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findEmployeesOnLeave(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(Employee.EmployeeStatus.ON_LEAVE)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findEmployeesOnProbation(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("probationEndDate").gte(LocalDate.now())
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findPendingOnboarding(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(Employee.EmployeeStatus.PENDING_ONBOARDING)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findPendingTermination(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(Employee.EmployeeStatus.PENDING_TERMINATION)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> searchByName(String tenantId, String firstName, String lastName) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .andOperator(
                    Criteria.where("firstName").regex(firstName, "i"),
                    Criteria.where("lastName").regex(lastName, "i")
                )
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> searchByKeyword(String tenantId, String keyword) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).orOperator(
                Criteria.where("firstName").regex(keyword, "i"),
                Criteria.where("lastName").regex(keyword, "i"),
                Criteria.where("email").regex(keyword, "i"),
                Criteria.where("position").regex(keyword, "i"),
                Criteria.where("department").regex(keyword, "i")
            )
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findBySkillsContaining(String tenantId, String skill) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("skills").is(skill)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByCertificationsContaining(String tenantId, String certification) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("certifications").is(certification)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByLanguagesContaining(String tenantId, String language) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("languages").is(language)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findUpcomingRetirements(String tenantId, LocalDate withinDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("birthDate").lte(withinDate.minusYears(65))
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findExpiringWorkPermits(String tenantId, LocalDate beforeDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("workPermitExpiry").lte(beforeDate.toString())
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public boolean existsByEmployeeNumberAndTenantId(String employeeNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("employeeNumber").is(employeeNumber).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Employee.class);
    }

    @Override
    public boolean existsByEmailAndTenantId(String email, String tenantId) {
        Query query = Query.query(
            Criteria.where("email").is(email).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Employee.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Employee.class);
    }

    @Override
    public void deleteByEmployeeNumberAndTenantId(String employeeNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("employeeNumber").is(employeeNumber).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Employee.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        mongoTemplate.remove(Query.query(Criteria.where("tenantId").is(tenantId)), Employee.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Employee.EmployeeStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public long countByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("department").is(department)
        );
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public long countByTenantIdAndLevel(String tenantId, Employee.EmployeeLevel level) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("level").is(level)
        );
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public long countByTenantIdAndEmploymentType(String tenantId, Employee.EmploymentType employmentType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("employmentType").is(employmentType)
        );
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public long countByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.count(query, Employee.class);
    }

    @Override
    public List<Employee> findDirectReports(String tenantId, String managerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("managerId").is(managerId)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByTenantIdAndActiveTrue(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("active").is(true)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findByManagerIdIsNull(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("managerId").is(null)
        );
        return mongoTemplate.find(query, Employee.class);
    }

    public Page<Employee> findByTenantId(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)).with(pageable);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        return new PageImpl<>(employees, pageable, employees.size());
    }

    public Page<Employee> searchByTenantIdAndSearchTerm(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).orOperator(
                Criteria.where("firstName").regex(searchTerm, "i"),
                Criteria.where("lastName").regex(searchTerm, "i"),
                Criteria.where("email").regex(searchTerm, "i"),
                Criteria.where("position").regex(searchTerm, "i")
            )
        ).with(pageable);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        return new PageImpl<>(employees, pageable, employees.size());
    }

    public long countByTenantIdAndDepartmentId(String tenantId, String departmentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId).and("departmentId").is(departmentId)
        );
        return mongoTemplate.count(query, Employee.class);
    }
}
