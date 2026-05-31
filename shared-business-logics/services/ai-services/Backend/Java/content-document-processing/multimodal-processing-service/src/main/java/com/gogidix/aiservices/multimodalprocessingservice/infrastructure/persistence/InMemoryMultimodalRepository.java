package com.gogidix.aiservices.multimodalprocessingservice.infrastructure.persistence;

import com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate.MultimodalContent;
import com.gogidix.aiservices.multimodalprocessingservice.domain.port.out.MultimodalRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryMultimodalRepository implements MultimodalRepository {

    private final Map<String, MultimodalContent> storage = new ConcurrentHashMap<>();

    @Override
    public MultimodalContent save(MultimodalContent content) {
        String key = content.getContentId().toString();
        storage.put(key, content);
        return content;
    }

    @Override
    public Optional<MultimodalContent> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<MultimodalContent> findByUserId(String userId) {
        return storage.values().stream()
                .filter(c -> c.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<MultimodalContent> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public void deleteByUserId(String userId) {
        storage.values().removeIf(c -> c.getUserId().equals(userId));
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public List<MultimodalContent> saveAll(List<MultimodalContent> contents) {
        contents.forEach(c -> storage.put(c.getContentId().toString(), c));
        return new ArrayList<>(contents);
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }
}

