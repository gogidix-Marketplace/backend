package com.gogidix.aiservices.aicontentgenerationservice.domain.port.out;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;

import java.util.List;
import java.util.Optional;

public interface ContentRepository {
    GeneratedContent save(GeneratedContent content);

    Optional<GeneratedContent> findById(String contentId);

    List<GeneratedContent> findByUserId(String userId);

    List<GeneratedContent> findByStatus(com.gogidix.aiservices.aicontentgenerationservice.domain.model.GenerationStatus status);

    void delete(String contentId);

    List<GeneratedContent> findRecentContent(String userId, int limit);
}
