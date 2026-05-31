package com.gogidix.ecommerce.sms.interfaces.rest;

import com.gogidix.ecommerce.sms.application.dto.CreateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.UpdateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.SmsResponse;
import com.gogidix.ecommerce.sms.application.service.SmsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/smss")
public class SmsController {

    private final SmsService service;

    public SmsController(SmsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SmsResponse> create(@Valid @RequestBody CreateSmsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmsResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SmsResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getList(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SmsResponse> update(@PathVariable String id, @RequestBody UpdateSmsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}