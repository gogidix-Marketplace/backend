package com.gogidix.hr.documentmanagement.infrastructure.persistence.mongo;

import com.gogidix.hr.documentmanagement.domain.model.HRDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRDocumentMongoRepository extends MongoRepository<HRDocument, String> {}
