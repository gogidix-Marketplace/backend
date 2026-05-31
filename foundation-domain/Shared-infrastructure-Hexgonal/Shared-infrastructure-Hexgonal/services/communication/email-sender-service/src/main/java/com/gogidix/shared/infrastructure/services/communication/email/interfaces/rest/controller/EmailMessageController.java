package com.gogidix.shared.infrastructure.services.communication.email.interfaces.rest.controller;

import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.in.IEmailMessageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for EmailMessage management
 */
@RestController
@RequestMapping("/api/v1/emails")
@CrossOrigin(origins = "*")
public class EmailMessageController {

    private final IEmailMessageUseCase emailPort;

    public EmailMessageController(IEmailMessageUseCase emailPort) {
        this.emailPort = emailPort;
    }

    @PostMapping
    public ResponseEntity<EmailMessageResponseDto> create(
            @Valid @RequestBody CreateEmailMessageRequestDto dto) {
        EmailMessageResponseDto created = emailPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailMessageResponseDto> getById(@PathVariable String id) {
        EmailMessageResponseDto entity = emailPort.findById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<EmailMessageResponseDto>> getAll() {
        List<EmailMessageResponseDto> entities = emailPort.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-to/{to}")
    public ResponseEntity<List<EmailMessageResponseDto>> getByTo(@PathVariable String to) {
        List<EmailMessageResponseDto> entities = emailPort.findByTo(to);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<EmailMessageResponseDto>> getByStatus(@PathVariable String status) {
        List<EmailMessageResponseDto> entities = emailPort.findByStatus(status);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-campaign/{campaignId}")
    public ResponseEntity<List<EmailMessageResponseDto>> getByCampaignId(@PathVariable String campaignId) {
        List<EmailMessageResponseDto> entities = emailPort.findByCampaignId(campaignId);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/stats")
    public ResponseEntity<IEmailMessageUseCase.EmailMessageStatsDto> getStats() {
        IEmailMessageUseCase.EmailMessageStatsDto stats = emailPort.getStats();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        emailPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanup(@RequestParam(defaultValue = "30") int daysToKeep) {
        emailPort.cleanupOldMessages(daysToKeep);
        return ResponseEntity.noContent().build();
    }
}
