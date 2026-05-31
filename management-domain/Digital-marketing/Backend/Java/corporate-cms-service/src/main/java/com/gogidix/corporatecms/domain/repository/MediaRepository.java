package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.MediaType;
import com.gogidix.corporatecms.domain.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Media entity.
 */
@Repository
public interface MediaRepository extends MongoRepository<Media, String> {

    Optional<Media> findByIdAndDeletedFalse(String id);

    List<Media> findByMediaTypeAndDeletedFalse(MediaType mediaType);

    Page<Media> findByMediaTypeAndDeletedFalse(MediaType mediaType, Pageable pageable);

    List<Media> findByFolderAndDeletedFalse(String folder);

    Page<Media> findByFolderAndDeletedFalse(String folder, Pageable pageable);

    List<Media> findByUploadedByAndDeletedFalse(String uploadedBy);

    @Query("{'fileName': {$regex: ?0, $options: 'i'}, 'deleted': false}")
    List<Media> searchByFileName(String keyword);

    @Query("{'$or': [" +
            "{'fileName': {$regex: ?0, $options: 'i'}}, " +
            "{'altText': {$regex: ?0, $options: 'i'}}, " +
            "{'description': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Media> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'optimized': false, 'deleted': false}")
    List<Media> findNonOptimizedMedia();

    List<Media> findByMimeTypeStartingWithAndDeletedFalse(String mimeTypePrefix);

    Long countByMediaTypeAndDeletedFalse(MediaType mediaType);

    Long countByUploadedByAndDeletedFalse(String uploadedBy);

    @Query("{'deleted': false}")
    Long countTotalMedia();

    @Query(value = "{'deleted': false}", count = true)
    Long getTotalSize();
}
