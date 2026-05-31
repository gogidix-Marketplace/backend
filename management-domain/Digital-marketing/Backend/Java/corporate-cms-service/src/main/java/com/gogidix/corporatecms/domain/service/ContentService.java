package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.application.dto.ContentDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.exception.*;
import com.gogidix.corporatecms.application.mapper.ContentMapper;
import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
import com.gogidix.corporatecms.domain.model.Content;
import com.gogidix.corporatecms.domain.model.Content.ContentVersion;
import com.gogidix.corporatecms.domain.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private static final Logger log = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;
    private final WorkflowService workflowService;

    private static final int MAX_VERSIONS = 10;

    @Transactional
    public ContentDTO createContent(ContentDTO dto, String authorId) {
        log.info("Creating content with slug: {}", dto.getSlug());

        if (contentRepository.findBySlugAndDeletedFalse(dto.getSlug()).isPresent()) {
            throw new DuplicateResourceException("Content", "slug", dto.getSlug());
        }

        Content content = contentMapper.toEntity(dto);
        content.setAuthorId(authorId);
        content.setStatus(ContentStatus.DRAFT);
        content.setPublished(false);

        Content savedContent = contentRepository.save(content);
        log.info("Content created with ID: {}", savedContent.getId());

        return contentMapper.toDto(savedContent);
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public ContentDTO updateContent(String id, ContentDTO dto, String userId) {
        log.info("Updating content with ID: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        contentMapper.updateEntityFromDto(dto, content);

        createVersion(content, "Content updated", userId);

        Content savedContent = contentRepository.save(content);
        log.info("Content updated: {}", id);

        return contentMapper.toDto(savedContent);
    }

    @Cacheable(value = "content", key = "#id")
    public ContentDTO getContentById(String id) {
        log.info("Fetching content by ID: {}", id);
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));
        return contentMapper.toDto(content);
    }

    public ContentDTO getContentBySlug(String slug) {
        log.info("Fetching content by slug: {}", slug);
        Content content = contentRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "slug", slug));

        if (content.getPublished()) {
            incrementViewCount(content);
        }

        return contentMapper.toDto(content);
    }

    public PageResponse<ContentDTO> getContentByType(ContentType type, int page, int size) {
        log.info("Fetching content by type: {}", type);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Content> contentPage = contentRepository.findByTypeAndDeletedFalse(type, pageable);
        return PageResponse.of(contentPage.map(contentMapper::toDto));
    }

    public PageResponse<ContentDTO> getContentByStatus(ContentStatus status, int page, int size) {
        log.info("Fetching content by status: {}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Content> contentPage = contentRepository.findByStatusAndDeletedFalse(status, pageable);
        return PageResponse.of(contentPage.map(contentMapper::toDto));
    }

    public PageResponse<ContentDTO> searchContent(String keyword, int page, int size) {
        log.info("Searching content with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Content> contentPage = contentRepository.searchByKeyword(keyword, pageable);
        return PageResponse.of(contentPage.map(contentMapper::toDto));
    }

    public PageResponse<ContentDTO> getPublishedContent(int page, int size) {
        log.info("Fetching published content");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Content> contentPage = contentRepository.findPublishedContent(pageable);
        return PageResponse.of(contentPage.map(contentMapper::toDto));
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public ContentDTO updateStatus(String id, ContentStatus newStatus, String userId) {
        log.info("Updating content status: {} -> {}", id, newStatus);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        if (!content.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidWorkflowTransitionException(
                    content.getStatus().name(), newStatus.name());
        }

        if (newStatus == ContentStatus.PENDING_APPROVAL) {
            workflowService.initiateWorkflow(id, userId, "Content submitted for approval");
        }

        content.setStatus(newStatus);

        if (newStatus == ContentStatus.PUBLISHED && !content.getPublished()) {
            publishContent(content, userId);
        }

        Content savedContent = contentRepository.save(content);
        log.info("Content status updated: {} -> {}", id, newStatus);

        return contentMapper.toDto(savedContent);
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public ContentDTO publishContent(String id, String userId) {
        log.info("Publishing content: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        if (!hasPublishPermission()) {
            throw new UnauthorizedException("You don't have permission to publish content");
        }

        publishContent(content, userId);

        Content savedContent = contentRepository.save(content);
        log.info("Content published: {}", id);

        return contentMapper.toDto(savedContent);
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public ContentDTO unpublishContent(String id, String userId) {
        log.info("Unpublishing content: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        content.setPublished(false);
        content.setStatus(ContentStatus.APPROVED);
        content.setUnpublishedAt(LocalDateTime.now());

        Content savedContent = contentRepository.save(content);
        log.info("Content unpublished: {}", id);

        return contentMapper.toDto(savedContent);
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public void deleteContent(String id, String userId) {
        log.info("Soft deleting content: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        if (!hasDeletePermission()) {
            throw new UnauthorizedException("You don't have permission to delete content");
        }

        content.setDeleted(true);
        content.setDeletedAt(LocalDateTime.now());
        contentRepository.save(content);

        log.info("Content deleted: {}", id);
    }

    @Transactional
    @CacheEvict(value = "content", key = "#id")
    public ContentDTO restoreContent(String id) {
        log.info("Restoring deleted content: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));

        content.setDeleted(false);
        content.setDeletedAt(null);
        content.setStatus(ContentStatus.DRAFT);

        Content savedContent = contentRepository.save(content);
        log.info("Content restored: {}", id);

        return contentMapper.toDto(savedContent);
    }

    public List<ContentDTO> getContentVersions(String id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", id));
        return contentMapper.toDtoList(List.of(content));
    }

    @Transactional
    public void publishScheduledContent() {
        log.info("Checking for scheduled content to publish");

        List<Content> scheduledContent = contentRepository.findScheduledContentToPublish(
                LocalDateTime.now(), ContentStatus.SCHEDULED);

        for (Content content : scheduledContent) {
            log.info("Publishing scheduled content: {}", content.getId());
            publishContent(content, "system");
            contentRepository.save(content);
        }

        log.info("Published {} scheduled content items", scheduledContent.size());
    }

    public List<ContentDTO> getContentByAuthor(String authorId) {
        List<Content> content = contentRepository.findByAuthorIdAndDeletedFalse(authorId);
        return contentMapper.toDtoList(content);
    }

    public List<ContentDTO> getContentByTags(List<String> tags) {
        List<Content> content = contentRepository.findByTagsIn(tags);
        return contentMapper.toDtoList(content);
    }

    private void publishContent(Content content, String userId) {
        content.setPublished(true);
        content.setStatus(ContentStatus.PUBLISHED);
        content.setPublishedAt(LocalDateTime.now());
    }

    private void createVersion(Content content, String comment, String userId) {
        Integer nextVersion = (content.getCurrentVersion() == null) ? 1 : content.getCurrentVersion() + 1;

        ContentVersion version = ContentVersion.builder()
                .versionNumber(nextVersion)
                .title(content.getTitle())
                .body(content.getBody())
                .summary(content.getSummary())
                .comment(comment)
                .authorId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        content.addVersion(version);

        if (content.getVersions().size() > MAX_VERSIONS) {
            content.getVersions().remove(0);
        }
    }

    private void incrementViewCount(Content content) {
        content.setViewCount((content.getViewCount() == null) ? 1 : content.getViewCount() + 1);
        contentRepository.save(content);
    }

    private boolean hasPublishPermission() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a instanceof SimpleGrantedAuthority
                        && (a.getAuthority().equals("CONTENT_PUBLISH")
                        || a.getAuthority().equals("CONTENT_APPROVE")));
    }

    private boolean hasDeletePermission() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a instanceof SimpleGrantedAuthority
                        && a.getAuthority().equals("CONTENT_DELETE"));
    }
}
