package com.gogidix.globalbusinessmanagement.countryingestion.domain.repository;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.DataSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DataSchema entity.
 * Provides data access operations for schema management.
 */
@Repository
public interface DataSchemaRepository extends MongoRepository<DataSchema, String> {

    /**
     * Find data schema by schema ID.
     */
    Optional<DataSchema> findBySchemaId(String schemaId);

    /**
     * Find data schema by schema name.
     */
    Optional<DataSchema> findBySchemaName(String schemaName);

    /**
     * Check if schema exists by schema ID.
     */
    boolean existsBySchemaId(String schemaId);

    /**
     * Find all active schemas.
     */
    List<DataSchema> findByActiveTrue();

    /**
     * Find all active schemas with pagination.
     */
    Page<DataSchema> findByActiveTrue(Pageable pageable);

    /**
     * Find all schemas by schema type.
     */
    List<DataSchema> findBySchemaType(DataSchema.SchemaType schemaType);

    /**
     * Find all active schemas by schema type.
     */
    List<DataSchema> findBySchemaTypeAndActiveTrue(DataSchema.SchemaType schemaType);

    /**
     * Find schemas by target entity.
     */
    List<DataSchema> findByTargetEntity(String targetEntity);

    /**
     * Find schemas by version.
     */
    List<DataSchema> findByVersion(String version);

    /**
     * Find schemas by schema name and version.
     */
    Optional<DataSchema> findBySchemaNameAndVersion(String schemaName, String version);

    /**
     * Find latest version of a schema by name.
     */
    @Query("{'schemaName': ?0, 'active': true}")
    List<DataSchema> findActiveVersionsByName(String schemaName);

    /**
     * Find schemas created by a specific user.
     */
    List<DataSchema> findByCreatedBy(String createdBy);

    /**
     * Find schemas by organization ID.
     */
    List<DataSchema> findByOrganizationId(String organizationId);

    /**
     * Find schemas created between dates.
     */
    List<DataSchema> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find schemas modified after a specific date.
     */
    List<DataSchema> findByLastModifiedDateAfter(LocalDateTime date);

    /**
     * Search schemas by name or description.
     */
    @Query("{$or: [" +
            "{'schemaName': {$regex: ?0, $options: 'i'}}, " +
            "{'description': {$regex: ?0, $options: 'i'}}" +
            "]}")
    Page<DataSchema> searchSchemas(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find schemas with strict validation enabled.
     */
    List<DataSchema> findByStrictValidationTrue();

    /**
     * Find schemas that allow unknown fields.
     */
    List<DataSchema> findByAllowUnknownFieldsTrue();

    /**
     * Find schemas by multiple criteria.
     */
    @Query("{$and: [" +
            "(?0 == null OR {'schemaType': ?0}), " +
            "(?1 == null OR {'active': ?1}), " +
            "(?2 == null OR {'targetEntity': ?2})" +
            "]}")
    List<DataSchema> findByMultipleCriteria(DataSchema.SchemaType schemaType,
                                             Boolean active,
                                             String targetEntity);

    /**
     * Count active schemas.
     */
    Long countByActiveTrue();

    /**
     * Count schemas by type.
     */
    Long countBySchemaType(DataSchema.SchemaType schemaType);

    /**
     * Get distinct schema types.
     */
    @Query(value = "{}", fields = "schemaType")
    List<DataSchema.SchemaType> findDistinctSchemaTypes();

    /**
     * Get distinct target entities.
     */
    @Query(value = "{}", fields = "targetEntity")
    List<String> findDistinctTargetEntities();

    /**
     * Find latest versions of all schemas.
     */
    @Query("{'active': true}")
    List<DataSchema> findLatestActiveSchemas(Pageable pageable);

    /**
     * Find all versions of a schema by schema name (excluding version suffix).
     */
    @Query("{'schemaName': {$regex: ?0, $options: 'i'}}")
    List<DataSchema> findAllVersionsByName(String schemaName);

    /**
     * Find schemas created by a specific user within date range.
     */
    List<DataSchema> findByCreatedByAndCreatedDateBetween(String createdBy,
                                                           LocalDateTime startDate,
                                                           LocalDateTime endDate);

    /**
     * Count schemas by created by user.
     */
    Long countByCreatedBy(String createdBy);

    /**
     * Find schemas with validation rules.
     */
    @Query("{'validationRules': {$ne: {}}}")
    List<DataSchema> findSchemasWithValidationRules();

    /**
     * Find schemas with transformation rules.
     */
    @Query("{'transformationRules': {$ne: {}}}")
    List<DataSchema> findSchemasWithTransformationRules();

    /**
     * Find schemas for a specific source format.
     */
    @Query("{'metadata.format': ?0}")
    List<DataSchema> findByFormat(String format);

    /**
     * Get schema usage statistics.
     */
    @Query("{$group: {_id: '$schemaType', count: {$sum: 1}}}")
    List<SchemaUsageStats> getUsageStatsByType();

    /**
     * Update schema active status.
     */
    @Query("{'schemaId': ?0}, {$set: {'active': ?1, 'lastModifiedDate': ?2}}")
    void updateSchemaActiveStatus(String schemaId, Boolean active, LocalDateTime lastModifiedDate);

    /**
     * Interface for schema usage statistics aggregation result.
     */
    interface SchemaUsageStats {
        String getId();
        Integer getCount();
    }
}
