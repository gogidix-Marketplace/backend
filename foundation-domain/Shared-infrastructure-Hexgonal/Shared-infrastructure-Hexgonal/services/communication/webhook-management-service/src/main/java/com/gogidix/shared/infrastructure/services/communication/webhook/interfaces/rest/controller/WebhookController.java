package com.gogidix.shared.infrastructure.services.communication.webhook.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.CreateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.request.UpdateWebhookRequestDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.application.dto.response.WebhookResponseDto;
import com.gogidix.shared.infrastructure.services.communication.webhook.domain.port.in.IWebhookUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/webhooks")
@CrossOrigin(origins = "*")
public class WebhookController {
    private final IWebhookUseCase webhookUseCase;
    public WebhookController(IWebhookUseCase webhookUseCase) {
        this.webhookUseCase = webhookUseCase;
    }
    @PostMapping
    public ResponseEntity<WebhookResponseDto> create(@Valid @RequestBody CreateWebhookRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(webhookUseCase.create(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<WebhookResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(webhookUseCase.findById(id));
    }
    @GetMapping
    public ResponseEntity<List<WebhookResponseDto>> getAll() {
        return ResponseEntity.ok(webhookUseCase.findAll());
    }
    @PutMapping("/{id}")
    public ResponseEntity<WebhookResponseDto> update(@PathVariable String id, @Valid @RequestBody UpdateWebhookRequestDto dto) {
        return ResponseEntity.ok(webhookUseCase.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        webhookUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable String id) {
        webhookUseCase.activate(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        webhookUseCase.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/trigger")
    public ResponseEntity<Void> trigger(@PathVariable String id) {
        webhookUseCase.trigger(id);
        return ResponseEntity.noContent().build();
    }
}
