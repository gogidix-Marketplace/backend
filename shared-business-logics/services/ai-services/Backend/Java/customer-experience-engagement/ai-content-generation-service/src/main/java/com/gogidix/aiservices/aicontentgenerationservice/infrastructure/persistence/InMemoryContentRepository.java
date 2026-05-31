package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.persistence;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GenerationStatus;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryContentRepository implements ContentRepository {

    private final Map<String, GeneratedContent> store = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> userContentIndex = new ConcurrentHashMap<>();

    @Override
    public GeneratedContent save(GeneratedContent content) {
        store.put(content.getContentId(), content);

        String userId = extractUserId(content);
        if (userId != null) {
            userContentIndex.computeIfAbsent(userId, k -> new HashSet<>())
                    .add(content.getContentId());
        }

        return content;
    }

    @Override
    public Optional<GeneratedContent> findById(String contentId) {
        return Optional.ofNullable(store.get(contentId));
    }

    @Override
    public List<GeneratedContent> findByUserId(String userId) {
        Set<String> contentIds = userContentIndex.get(userId);
        if (contentIds == null || contentIds.isEmpty()) {
            return List.of();
        }

        return contentIds.stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneratedContent> findByStatus(GenerationStatus status) {
        return store.values().stream()
                .filter(content -> content.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String contentId) {
        GeneratedContent content = store.remove(contentId);
        if (content != null) {
            String userId = extractUserId(content);
            if (userId != null) {
                Set<String> contentIds = userContentIndex.get(userId);
                if (contentIds != null) {
                    contentIds.remove(contentId);
                    if (contentIds.isEmpty()) {
                        userContentIndex.remove(userId);
                    }
                }
            }
        }
    }

    @Override
    public List<GeneratedContent> findRecentContent(String userId, int limit) {
        return findByUserId(userId).stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private String extractUserId(GeneratedContent content) {
        return null;
    }
}
