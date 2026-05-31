package com.gogidix.shared.infrastructure.services.communication.messagequeue.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.request.CreateMessageQueueRequestDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.response.MessageQueueResponseDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.in.IMessageQueueUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/message-queues")
@CrossOrigin(origins = "*")
public class MessageQueueController {
    private final IMessageQueueUseCase queueUseCase;
    public MessageQueueController(IMessageQueueUseCase queueUseCase) {
        this.queueUseCase = queueUseCase;
    }
    @PostMapping
    public ResponseEntity<MessageQueueResponseDto> create(@Valid @RequestBody CreateMessageQueueRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(queueUseCase.create(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<MessageQueueResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(queueUseCase.findById(id));
    }
    @GetMapping
    public ResponseEntity<List<MessageQueueResponseDto>> getAll() {
        return ResponseEntity.ok(queueUseCase.findAll());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        queueUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
