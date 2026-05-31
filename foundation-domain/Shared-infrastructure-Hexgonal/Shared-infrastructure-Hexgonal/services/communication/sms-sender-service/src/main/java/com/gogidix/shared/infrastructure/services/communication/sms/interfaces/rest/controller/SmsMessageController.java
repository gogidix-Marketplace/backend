package com.gogidix.shared.infrastructure.services.communication.sms.interfaces.rest.controller;

import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request.CreateSmsMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response.SmsMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.in.ISmsMessageUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for SmsMessage management
 */
@RestController
@RequestMapping("/api/v1/sms")
@CrossOrigin(origins = "*")
public class SmsMessageController {

    private final ISmsMessageUseCase smsPort;

    public SmsMessageController(ISmsMessageUseCase smsPort) {
        this.smsPort = smsPort;
    }

    @PostMapping
    public ResponseEntity<SmsMessageResponseDto> create(
            @Valid @RequestBody CreateSmsMessageRequestDto dto) {
        SmsMessageResponseDto created = smsPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmsMessageResponseDto> getById(@PathVariable String id) {
        SmsMessageResponseDto entity = smsPort.findById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<SmsMessageResponseDto>> getAll() {
        List<SmsMessageResponseDto> entities = smsPort.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-phone/{phoneNumber}")
    public ResponseEntity<List<SmsMessageResponseDto>> getByPhoneNumber(@PathVariable String phoneNumber) {
        List<SmsMessageResponseDto> entities = smsPort.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<SmsMessageResponseDto>> getByStatus(@PathVariable String status) {
        List<SmsMessageResponseDto> entities = smsPort.findByStatus(status);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/by-campaign/{campaignId}")
    public ResponseEntity<List<SmsMessageResponseDto>> getByCampaignId(@PathVariable String campaignId) {
        List<SmsMessageResponseDto> entities = smsPort.findByCampaignId(campaignId);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/stats")
    public ResponseEntity<ISmsMessageUseCase.SmsMessageStatsDto> getStats() {
        ISmsMessageUseCase.SmsMessageStatsDto stats = smsPort.getStats();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        smsPort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanup(@RequestParam(defaultValue = "30") int daysToKeep) {
        smsPort.cleanupOldMessages(daysToKeep);
        return ResponseEntity.noContent().build();
    }
}
