package com.gogidix.digitalmarketing.contentmanagement.domain.repository;

import com.gogidix.digitalmarketing.contentmanagement.domain.model.ContentPiece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentPieceRepository extends MongoRepository<ContentPiece, String> {
    List<ContentPiece> findByTenantId(String tenantId);
}