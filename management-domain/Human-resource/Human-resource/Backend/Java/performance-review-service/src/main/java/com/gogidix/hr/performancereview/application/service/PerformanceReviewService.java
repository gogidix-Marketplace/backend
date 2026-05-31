package com.gogidix.hr.performancereview.application.service;

import com.gogidix.hr.performancereview.domain.model.PerformanceReview;
import com.gogidix.hr.performancereview.domain.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceReviewService {
    private final PerformanceReviewRepository repository;

    public PerformanceReview create(PerformanceReview review) { return repository.save(review); }
    public PerformanceReview getById(String id) { return repository.findById(id).orElse(null); }
    public List<PerformanceReview> getAll() { return repository.findAll(); }
    public PerformanceReview update(PerformanceReview review) { return repository.save(review); }
    public void delete(String id) { repository.deleteById(id); }
}
