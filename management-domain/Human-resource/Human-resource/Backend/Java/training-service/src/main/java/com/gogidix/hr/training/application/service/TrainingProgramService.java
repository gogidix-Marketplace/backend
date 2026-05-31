package com.gogidix.hr.training.application.service;

import com.gogidix.hr.training.domain.model.TrainingProgram;
import com.gogidix.hr.training.domain.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingProgramService {
    private final TrainingProgramRepository repository;

    public TrainingProgram create(TrainingProgram program) { return repository.save(program); }
    public TrainingProgram getById(String id) { return repository.findById(id).orElse(null); }
    public List<TrainingProgram> getAll() { return repository.findAll(); }
    public TrainingProgram update(TrainingProgram program) { return repository.save(program); }
    public void delete(String id) { repository.deleteById(id); }
}
