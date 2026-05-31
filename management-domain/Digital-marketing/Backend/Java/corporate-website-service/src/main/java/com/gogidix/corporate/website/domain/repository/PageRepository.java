package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PageRepository {
    Page save(Page page);
    Optional<Page> findById(String id);
    Optional<Page> findByPageKey(String pageKey);
    Optional<Page> findByPath(String path);
    List<Page> findByStatus(ContentStatus status);
    List<Page> findPublishedPages();
    List<Page> findPublishedPagesByRegion(Region region);
    List<Page> findNavigationPages();
    List<Page> findByParentPageId(String parentPageId);
    List<Page> findByTagsContaining(String tag);
    List<Page> searchByKeyword(String keyword, Language language);
    void deleteById(String id);
    boolean existsByPageKey(String pageKey);
    boolean existsByPath(String path);
    List<Page> findPagesScheduledForPublish(LocalDateTime now);
    List<Page> findPagesToUnpublish(LocalDateTime now);
    long countByStatus(ContentStatus status);
}
