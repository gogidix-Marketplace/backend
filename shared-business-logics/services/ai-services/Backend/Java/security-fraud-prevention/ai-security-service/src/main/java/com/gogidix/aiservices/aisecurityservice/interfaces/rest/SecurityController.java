package com.gogidix.aiservices.aisecurityservice.interfaces.rest;

import com.gogidix.aiservices.aisecurityservice.application.dto.request.EncryptionRequest;
import com.gogidix.aiservices.aisecurityservice.application.dto.request.DecryptionRequest;
import com.gogidix.aiservices.aisecurityservice.application.dto.response.EncryptionResponse;
import com.gogidix.aiservices.aisecurityservice.application.dto.response.DecryptionResponse;
import com.gogidix.aiservices.aisecurityservice.application.service.SecurityService;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionKey;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptionResponse> encrypt(@Valid @RequestBody EncryptionRequest request) {
        EncryptionResult result = securityService.encrypt(
                request.getData(),
                request.getAlgorithm()
        );
        return ResponseEntity.ok(EncryptionResponse.fromDomain(result));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<DecryptionResponse> decrypt(@Valid @RequestBody DecryptionRequest request) {
        String decryptedData = securityService.decrypt(
                request.getEncryptedData(),
                request.getKeyId()
        );
        return ResponseEntity.ok(new DecryptionResponse(decryptedData));
    }

    @PostMapping("/keys/rotate")
    public ResponseEntity<Void> rotateKeys() {
        securityService.rotateKeys();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/keys/{keyId}")
    public ResponseEntity<EncryptionKey> getKey(@PathVariable String keyId) {
        return ResponseEntity.ok(securityService.getKey(keyId));
    }

    @GetMapping("/keys")
    public ResponseEntity<Set<EncryptionKey>> getAllKeys() {
        return ResponseEntity.ok(securityService.getAllKeys());
    }
}
