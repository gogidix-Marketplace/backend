package com.gogidix.shared.audit.adapter.out.persistence;

import com.gogidix.shared.audit.domain.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditEventJpaRepository extends JpaRepository<AuditEvent, String> {
    
    @Modifying
    @Query("DELETE FROM AuditEvent a WHERE a.timestamp < :cutoffDate")
    void deleteByTimestampBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}