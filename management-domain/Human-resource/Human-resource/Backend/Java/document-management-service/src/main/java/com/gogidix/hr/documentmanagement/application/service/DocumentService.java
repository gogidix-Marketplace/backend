package com.gogidix.hr.documentmanagement.application.service;

import com.gogidix.hr.documentmanagement.domain.model.HRDocument;
import com.gogidix.hr.documentmanagement.infrastructure.persistence.mongo.HRDocumentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final HRDocumentMongoRepository repository;

    public HRDocument create(HRDocument doc) { return repository.save(doc); }
    public HRDocument getById(String id) { return repository.findById(id).orElse(null); }
    public List<HRDocument> getAll() { return repository.findAll(); }
    public HRDocument update(HRDocument doc) { return repository.save(doc); }
    public void delete(String id) { repository.deleteById(id); }
}
