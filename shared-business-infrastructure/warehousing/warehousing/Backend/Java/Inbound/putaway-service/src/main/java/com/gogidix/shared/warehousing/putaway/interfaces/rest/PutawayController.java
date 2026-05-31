package com.gogidix.shared.warehousing.putaway.interfaces.rest;

import com.gogidix.shared.warehousing.putaway.application.command.CreatePutawayTaskCommand;
import com.gogidix.shared.warehousing.putaway.application.dto.PutawayTaskDTO;
import com.gogidix.shared.warehousing.putaway.application.service.PutawayService;
import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Putaway Tasks", description = "Put-away task management APIs")
public class PutawayController {

    private final PutawayService putawayService;

    @PostMapping
    @Operation(summary = "Create put-away task")
    public ResponseEntity<PutawayTaskDTO> createTask(@Valid @RequestBody CreatePutawayTaskCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(putawayService.createTask(command));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending tasks")
    public ResponseEntity<List<PutawayTaskDTO>> getPendingTasks() {
        return ResponseEntity.ok(putawayService.getPendingTasks());
    }

    @GetMapping("/receipt/{receiptId}")
    @Operation(summary = "Get tasks by receipt")
    public ResponseEntity<List<PutawayTaskDTO>> getTasksByReceipt(@PathVariable String receiptId) {
        return ResponseEntity.ok(putawayService.getTasksByReceipt(receiptId));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete task")
    public ResponseEntity<PutawayTaskDTO> completeTask(@PathVariable String id) {
        return ResponseEntity.ok(putawayService.completeTask(id));
    }
}
