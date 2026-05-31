package com.gogidix.hr.employee.interfaces.rest;

import com.gogidix.hr.employee.application.service.EmployeeService;
import com.gogidix.hr.employee.domain.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody CreateEmployeeRequest request) {
        Employee emp = service.createEmployee(request.firstName, request.lastName, request.email,
                request.departmentId, request.position, request.level, request.employmentType,
                request.hireDate, request.createdBy);
        return ResponseEntity.ok(emp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable String id) {
        return service.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{employeeNumber}")
    public ResponseEntity<Employee> getByNumber(@PathVariable String employeeNumber) {
        return service.getEmployeeByNumber(employeeNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> search(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.searchEmployees(searchTerm, PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActive() {
        return ResponseEntity.ok(service.getActiveEmployees());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> getByDepartment(@PathVariable String departmentId) {
        return ResponseEntity.ok(service.getEmployeesByDepartment(departmentId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Employee>> getByManager(@PathVariable String managerId) {
        return ResponseEntity.ok(service.getEmployeesByManager(managerId));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Employee> activate(@PathVariable String id) {
        return ResponseEntity.ok(service.activateEmployee(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Employee> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(service.deactivateEmployee(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    public static class CreateEmployeeRequest {
        public String firstName;
        public String lastName;
        public String email;
        public String departmentId;
        public String position;
        public Employee.EmployeeLevel level;
        public Employee.EmploymentType employmentType;
        public java.time.LocalDate hireDate;
        public String createdBy;
    }
}
