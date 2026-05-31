package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageDomainService {

    private final PageRepository pageRepository;

    @CacheEvict(value = "pages", allEntries = true)
    public Page createPage(Page page) {
        validatePage(page);
        return pageRepository.save(page);
    }

    @CacheEvict(value = "pages", allEntries = true)
    public Page updatePage(Page page) {
        validatePage(page);
        return pageRepository.save(page);
    }

    @CacheEvict(value = "pages", allEntries = true)
    public void deletePage(String id) {
        pageRepository.deleteById(id);
    }

    @Cacheable(value = "pages", key = "#id")
    public Optional<Page> getPageById(String id) {
        return pageRepository.findById(id);
    }

    @Cacheable(value = "pages", key = "'key:' + #pageKey")
    public Optional<Page> getPageByKey(String pageKey) {
        return pageRepository.findByPageKey(pageKey);
    }

    @Cacheable(value = "pages", key = "'path:' + #path")
    public Optional<Page> getPageByPath(String path) {
        return pageRepository.findByPath(path);
    }

    @Cacheable(value = "pages", key = "'published:' + #region")
    public List<Page> getPublishedPagesByRegion(Region region) {
        return pageRepository.findPublishedPagesByRegion(region);
    }

    @Cacheable(value = "pages", key = "'navigation:' + #region")
    public List<Page> getNavigationPages(Region region) {
        return pageRepository.findNavigationPages().stream()
                .filter(page -> page.getAvailableRegions().contains(region))
                .sorted((p1, p2) -> {
                    if (p1.getSortOrder() == null && p2.getSortOrder() == null) return 0;
                    if (p1.getSortOrder() == null) return 1;
                    if (p2.getSortOrder() == null) return -1;
                    return p1.getSortOrder().compareTo(p2.getSortOrder());
                })
                .toList();
    }

    public List<Page> searchPages(String keyword, Language language) {
        return pageRepository.searchByKeyword(keyword, language);
    }

    public List<Page> getPagesByTag(String tag) {
        return pageRepository.findByTagsContaining(tag);
    }

    public List<Page> getChildPages(String parentPageId) {
        return pageRepository.findByParentPageId(parentPageId);
    }

    public List<Page> getScheduledPages() {
        return pageRepository.findPagesScheduledForPublish(LocalDateTime.now());
    }

    public List<Page> getPagesToUnpublish() {
        return pageRepository.findPagesToUnpublish(LocalDateTime.now());
    }

    @CacheEvict(value = "pages", allEntries = true)
    public Page publishPage(String id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Page not found: " + id));
        page.setStatus(ContentStatus.PUBLISHED);
        page.setPublishDate(LocalDateTime.now());
        return pageRepository.save(page);
    }

    @CacheEvict(value = "pages", allEntries = true)
    public Page unpublishPage(String id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Page not found: " + id));
        page.setStatus(ContentStatus.ARCHIVED);
        return pageRepository.save(page);
    }

    private void validatePage(Page page) {
        if (page.getPageKey() == null || page.getPageKey().isBlank()) {
            throw new IllegalArgumentException("Page key cannot be null or blank");
        }
        if (page.getPath() == null || page.getPath().isBlank()) {
            throw new IllegalArgumentException("Page path cannot be null or blank");
        }
        if (page.getLocalizedContent() == null || page.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Page must have at least one localized content");
        }
    }

    public long getPageCount() {
        return pageRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}
