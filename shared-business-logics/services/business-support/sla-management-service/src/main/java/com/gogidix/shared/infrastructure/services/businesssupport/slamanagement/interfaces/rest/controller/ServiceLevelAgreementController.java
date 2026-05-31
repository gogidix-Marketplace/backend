package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.in.IServiceLevelAgreementUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for ServiceLevelAgreement management
 */
@RestController
@RequestMapping("/api/v1/service-level-agreements")
@CrossOrigin(origins = "*")
public class ServiceLevelAgreementController {
    private final IServiceLevelAgreementUseCase slaUseCase;
    public ServiceLevelAgreementController(IServiceLevelAgreementUseCase slaUseCase) {
        this.slaUseCase = slaUseCase;
    }
    @PostMapping
    public ResponseEntity<ServiceLevelAgreementResponseDto> create(
            @Valid @RequestBody CreateServiceLevelAgreementRequestDto dto) {
        ServiceLevelAgreementResponseDto created = slaUseCase.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ServiceLevelAgreementResponseDto> getById(@PathVariable String id) {
        ServiceLevelAgreementResponseDto sla = slaUseCase.findById(id);
        return ResponseEntity.ok(sla);
    }
    @GetMapping
    public ResponseEntity<List<ServiceLevelAgreementResponseDto>> getAll() {
        List<ServiceLevelAgreementResponseDto> slas = slaUseCase.findAll();
        return ResponseEntity.ok(slas);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceLevelAgreementResponseDto>> getByStatus(@PathVariable String status) {
        List<ServiceLevelAgreementResponseDto> slas = slaUseCase.findByStatus(status);
        return ResponseEntity.ok(slas);
    }
    @GetMapping("/service-type/{serviceType}")
    public ResponseEntity<List<ServiceLevelAgreementResponseDto>> getByServiceType(@PathVariable String serviceType) {
        List<ServiceLevelAgreementResponseDto> slas = slaUseCase.findByServiceType(serviceType);
        return ResponseEntity.ok(slas);
    }
    @GetMapping("/active")
    public ResponseEntity<List<ServiceLevelAgreementResponseDto>> getActive() {
        List<ServiceLevelAgreementResponseDto> slas = slaUseCase.findActive();
        return ResponseEntity.ok(slas);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ServiceLevelAgreementResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateServiceLevelAgreementRequestDto dto) {
        ServiceLevelAgreementResponseDto updated = slaUseCase.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        slaUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        slaUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
