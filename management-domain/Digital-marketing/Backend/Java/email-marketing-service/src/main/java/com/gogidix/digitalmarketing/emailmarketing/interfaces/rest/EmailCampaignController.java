package com.gogidix.digitalmarketing.emailmarketing.interfaces.rest;

import com.gogidix.digitalmarketing.emailmarketing.application.service.EmailCampaignService;
import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailCampaign;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/email/campaigns")
@RequiredArgsConstructor
public class EmailCampaignController {
    private final EmailCampaignService service;

    @PostMapping
    public ResponseEntity<EmailCampaign> create(@RequestBody EmailCampaign entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<EmailCampaign>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailCampaign> getById(@PathVariable String id) {
        EmailCampaign result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailCampaign> update(@PathVariable String id, @RequestBody EmailCampaign entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
