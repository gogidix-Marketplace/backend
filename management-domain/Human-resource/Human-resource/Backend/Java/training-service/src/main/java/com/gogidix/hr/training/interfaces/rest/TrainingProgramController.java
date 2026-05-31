package com.gogidix.hr.training.interfaces.rest;

import com.gogidix.hr.training.application.service.TrainingProgramService;
import com.gogidix.hr.training.domain.model.TrainingProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training/programs")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService service;

    @PostMapping
    public ResponseEntity<TrainingProgram> create(@RequestBody TrainingProgram entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgram>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgram> getById(@PathVariable String id) {
        TrainingProgram result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgram> update(@PathVariable String id, @RequestBody TrainingProgram entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
