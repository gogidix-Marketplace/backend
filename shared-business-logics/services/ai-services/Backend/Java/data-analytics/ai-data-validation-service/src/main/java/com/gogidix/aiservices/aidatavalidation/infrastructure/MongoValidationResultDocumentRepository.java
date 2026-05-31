package com.gogidix.aiservices.aidatavalidation.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for ValidationResultDocument.
 */
@Repository
public interface MongoValidationResultDocumentRepository extends MongoRepository<ValidationResultDocument, String> {

    Optional<ValidationResultDocument> findByValidationId(String validationId);

    List<ValidationResultDocument> findByValid(boolean isValid);

    void deleteByValidationId(String validationId);

    List<ValidationResultDocument> findTop5ByOrderByValidatedAtDesc();

    long countByValid(boolean isValid);
}
