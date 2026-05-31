package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.persistence;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryLeadDataSource {

    private final Map<String, LeadEntity> byId = new ConcurrentHashMap<>();
    private final Map<String, LeadEntity> byEmail = new ConcurrentHashMap<>();
    private final Map<String, List<LeadEntity>> byStatus = new ConcurrentHashMap<>();
    private final Map<String, List<LeadEntity>> byOwner = new ConcurrentHashMap<>();

    public LeadEntity save(LeadEntity entity) {
        LeadEntity savedEntity = entity;
        if (savedEntity.getLeadId() == null) {
            savedEntity = new LeadEntity();
        }

        savedEntity.setUpdatedAt(Instant.now());

        String leadId = savedEntity.getLeadId();
        byId.put(leadId, savedEntity);
        byEmail.put(savedEntity.getEmail(), savedEntity);

        byStatus.computeIfAbsent(savedEntity.getStatus().toString(), k -> new ArrayList<>())
                .removeIf(e -> e.getLeadId().equals(leadId));
        byStatus.get(savedEntity.getStatus().toString()).add(savedEntity);

        if (savedEntity.getOwnerId() != null) {
            String ownerId = savedEntity.getOwnerId();
            byOwner.computeIfAbsent(ownerId, k -> new ArrayList<>())
                    .removeIf(e -> e.getLeadId().equals(leadId));
            byOwner.get(ownerId).add(savedEntity);
        }

        return savedEntity;
    }

    public Optional<LeadEntity> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public Optional<LeadEntity> findByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email));
    }

    public List<LeadEntity> findAll() {
        return new ArrayList<>(byId.values());
    }

    public List<LeadEntity> findByStatus(LeadStatus status) {
        return byStatus.getOrDefault(status.toString(), new ArrayList<>());
    }

    public List<LeadEntity> findByOwnerId(String ownerId) {
        return byOwner.getOrDefault(ownerId, new ArrayList<>());
    }

    public List<LeadEntity> findByCreatedAtAfter(Instant timestamp) {
        return byId.values().stream()
                .filter(e -> e.getCreatedAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    public List<LeadEntity> findTopScores(int limit) {
        return byId.values().stream()
                .filter(e -> e.getScore() != null)
                .sorted((e1, e2) -> Double.compare(e2.getScore(), e1.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        LeadEntity entity = byId.remove(id);
        if (entity != null) {
            byEmail.remove(entity.getEmail());
            byStatus.getOrDefault(entity.getStatus().toString(), new ArrayList<>())
                    .removeIf(e -> e.getLeadId().equals(id));
            if (entity.getOwnerId() != null) {
                byOwner.getOrDefault(entity.getOwnerId(), new ArrayList<>())
                        .removeIf(e -> e.getLeadId().equals(id));
            }
        }
    }

    public List<LeadEntity> saveAll(List<LeadEntity> entities) {
        List<LeadEntity> saved = new ArrayList<>();
        for (LeadEntity entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }
}
