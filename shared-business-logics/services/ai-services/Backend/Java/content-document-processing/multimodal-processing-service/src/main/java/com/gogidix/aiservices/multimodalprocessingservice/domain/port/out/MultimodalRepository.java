package com.gogidix.aiservices.multimodalprocessingservice.domain.port.out;

import com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate.MultimodalContent;

import java.util.List;
import java.util.Optional;

import com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate.MultimodalContent;

import java.util.List;
import java.util.Optional;

public interface MultimodalRepository {
    MultimodalContent save(MultimodalContent content);
    Optional<MultimodalContent> findById(String id);
    List<MultimodalContent> findAll();
    List<MultimodalContent> findByUserId(String userId);
    void deleteById(String id);
    void deleteByUserId(String userId);
    long count();
    List<MultimodalContent> saveAll(List<MultimodalContent> contents);
    void deleteAll();
    boolean existsById(String id);
}
