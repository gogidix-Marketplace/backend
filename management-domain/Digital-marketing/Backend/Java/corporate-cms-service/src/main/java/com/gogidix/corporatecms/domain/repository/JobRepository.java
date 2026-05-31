package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.JobStatus;
import com.gogidix.corporatecms.domain.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Job entity.
 */
@Repository
public interface JobRepository extends MongoRepository<Job, String> {

    Optional<Job> findBySlugAndDeletedFalse(String slug);

    List<Job> findByStatusAndDeletedFalse(JobStatus status);

    Page<Job> findByStatusAndDeletedFalse(JobStatus status, Pageable pageable);

    Page<Job> findByDepartmentIdAndDeletedFalse(String departmentId, Pageable pageable);

    Page<Job> findByPublishedTrueAndDeletedFalse(Pageable pageable);

    @Query("{'$or': [" +
            "{'title': {$regex: ?0, $options: 'i'}}, " +
            "{'summary': {$regex: ?0, $options: 'i'}}, " +
            "{'description': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Job> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'$or': [" +
            "{'location': {$regex: ?0, $options: 'i'}}, " +
            "{'locationType': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Job> searchByLocation(String location, Pageable pageable);

    @Query("{'skills': {$in: ?0}, 'deleted': false}")
    List<Job> findBySkillsIn(List<String> skills);

    @Query("{'status': 'OPEN', 'published': true, 'applicationDeadline': {$gte: ?0}, 'deleted': false}")
    Page<Job> findOpenJobsWithDeadlineAfter(LocalDateTime date, Pageable pageable);

    @Query("{'featured': true, 'status': 'OPEN', 'published': true, 'deleted': false}")
    List<Job> findFeaturedJobs();

    Long countByStatusAndDeletedFalse(JobStatus status);

    @Query("{'status': 'OPEN', 'deleted': false}")
    Long countOpenJobs();
}
