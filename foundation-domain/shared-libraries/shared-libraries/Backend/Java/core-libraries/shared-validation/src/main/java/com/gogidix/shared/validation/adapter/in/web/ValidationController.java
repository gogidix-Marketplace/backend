package com.gogidix.shared.validation.adapter.in.web;

import com.gogidix.shared.validation.application.port.in.ValidationUseCase;
import com.gogidix.shared.validation.domain.model.ValidationRequest;
import com.gogidix.shared.validation.domain.model.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/validation")
public class ValidationController {

    private final ValidationUseCase validationUseCase;

    @Autowired
    public ValidationController(ValidationUseCase validationUseCase) {
        this.validationUseCase = validationUseCase;
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validate(@Valid @RequestBody ValidationRequest request) {
        ValidationResult result = validationUseCase.validate(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Validation service is healthy");
    }
}