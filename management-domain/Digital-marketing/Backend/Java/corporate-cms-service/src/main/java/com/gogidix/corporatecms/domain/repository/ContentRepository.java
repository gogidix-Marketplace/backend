package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
import com.gogidix.corporatecms.domain.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Content entity.
 */
@Repository
public interface ContentRepository extends MongoRepository<Content, String> {

    Optional<Content> findBySlugAndDeletedFalse(String slug);

    List<Content> findByTypeAndDeletedFalse(ContentType type);

    Page<Content> findByTypeAndDeletedFalse(ContentType type, Pageable pageable);

    Page<Content> findByStatusAndDeletedFalse(ContentStatus status, Pageable pageable);

    Page<Content> findByTypeAndStatusAndDeletedFalse(ContentType type, ContentStatus status, Pageable pageable);

    List<Content> findByAuthorIdAndDeletedFalse(String authorId);

    List<Content> findByCategoryIdAndDeletedFalse(String categoryId);

    @Query("{'tags': {$in: ?0}, 'deleted': false}")
    List<Content> findByTagsIn(List<String> tags);

    @Query("{'tags': {$all: ?0}, 'deleted': false}")
    List<Content> findByTagsAll(List<String> tags);

    @Query("{'$or': [" +
            "{'title': {$regex: ?0, $options: 'i'}}, " +
            "{'summary': {$regex: ?0, $options: 'i'}}, " +
            "{'body': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Content> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'scheduledPublishAt': {$lte: ?0, $ne: null}, 'status': ?1, 'deleted': false}")
    List<Content> findScheduledContentToPublish(LocalDateTime now, ContentStatus scheduled);

    @Query("{'published': true, 'deleted': false}")
    Page<Content> findPublishedContent(Pageable pageable);

    @Query("{'published': true, 'type': ?0, 'deleted': false}")
    Page<Content> findPublishedByType(ContentType type, Pageable pageable);

    List<Content> findByFeaturedImageIdAndDeletedFalse(String mediaId);

    @Query("{'mediaIds': {$in: ?0}, 'deleted': false}")
    List<Content> findByMediaIdIn(String mediaId);

    Long countByAuthorIdAndDeletedFalse(String authorId);

    Long countByTypeAndDeletedFalse(ContentType type);

    Long countByStatusAndDeletedFalse(ContentStatus status);

    @Query("{'published': true, 'deleted': false}")
    Long countPublishedContent();
}
