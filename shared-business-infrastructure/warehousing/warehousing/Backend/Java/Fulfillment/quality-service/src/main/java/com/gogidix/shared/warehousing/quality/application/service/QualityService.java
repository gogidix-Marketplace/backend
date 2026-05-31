package com.gogidix.shared.warehousing.quality.application.service;

import com.gogidix.shared.warehousing.quality.application.command.CreateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.command.UpdateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.dto.QualityCheckDTO;
import com.gogidix.shared.warehousing.quality.application.mapper.QualityMapper;
import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import com.gogidix.shared.warehousing.quality.domain.events.QualityCheckCompletedEvent;
import com.gogidix.shared.warehousing.quality.domain.exception.QualityCheckNotFoundException;
import com.gogidix.shared.warehousing.quality.domain.repository.QualityCheckRepository;
import com.gogidix.shared.warehousing.quality.infrastructure.messaging.QualityEventPublisher;
import com.gogidix.shared.warehousing.quality.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QualityService {

    private final QualityCheckRepository qualityCheckRepository;
    private final QualityMapper qualityMapper;
    private final QualityEventPublisher eventPublisher;

    public QualityCheckDTO createQualityCheck(CreateQualityCheckCommand command) {
        log.info("Creating quality check for reference: {}", command.getReferenceId());

        String tenantId = TenantContext.getCurrentTenantId();

        QualityCheck check = qualityMapper.toEntity(command);
        check.setTenantId(tenantId);
        check.setStatus(QualityCheck.QualityStatus.PENDING);
        check.setInspectionDate(LocalDateTime.now());

        QualityCheck savedCheck = qualityCheckRepository.save(check);

        log.info("Quality check created with ID: {}", savedCheck.getId());
        return qualityMapper.toDTO(savedCheck);
    }

    @Transactional(readOnly = true)
    public QualityCheckDTO getQualityCheck(String id) {
        QualityCheck check = qualityCheckRepository.findById(id)
            .orElseThrow(() -> new QualityCheckNotFoundException(id));
        return qualityMapper.toDTO(check);
    }

    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getAllQualityChecks() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<QualityCheck> checks = qualityCheckRepository.findByTenantId(tenantId);
        return qualityMapper.toDTOList(checks);
    }

    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getChecksByReference(String referenceId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<QualityCheck> checks = qualityCheckRepository.findByTenantIdAndReferenceId(tenantId, referenceId);
        return qualityMapper.toDTOList(checks);
    }

    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getChecksByStatus(QualityCheck.QualityStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<QualityCheck> checks = qualityCheckRepository.findByTenantIdAndStatus(tenantId, status);
        return qualityMapper.toDTOList(checks);
    }

    public QualityCheckDTO updateQualityCheck(String id, UpdateQualityCheckCommand command) {
        log.info("Updating quality check: {}", id);

        QualityCheck check = qualityCheckRepository.findById(id)
            .orElseThrow(() -> new QualityCheckNotFoundException(id));

        qualityMapper.updateEntity(check, command);
        QualityCheck updatedCheck = qualityCheckRepository.save(check);

        log.info("Quality check updated: {}", id);
        return qualityMapper.toDTO(updatedCheck);
    }

    public QualityCheckDTO completeQualityCheck(String id, Boolean passed, Integer score) {
        log.info("Completing quality check: {} with result: {}", id, passed);

        QualityCheck check = qualityCheckRepository.findById(id)
            .orElseThrow(() -> new QualityCheckNotFoundException(id));

        check.setStatus(passed ? QualityCheck.QualityStatus.PASSED : QualityCheck.QualityStatus.FAILED);
        check.setPassed(passed);
        check.setQualityScore(score);

        QualityCheck updatedCheck = qualityCheckRepository.save(check);

        QualityCheckCompletedEvent event = QualityCheckCompletedEvent.builder()
            .qualityCheckId(updatedCheck.getId())
            .referenceId(updatedCheck.getReferenceId())
            .referenceType(updatedCheck.getReferenceType().name())
            .passed(updatedCheck.getPassed())
            .qualityScore(updatedCheck.getQualityScore())
            .tenantId(updatedCheck.getTenantId())
            .build();
        eventPublisher.publishQualityCheckCompleted(event);

        log.info("Quality check completed: {}", id);
        return qualityMapper.toDTO(updatedCheck);
    }

    public void deleteQualityCheck(String id) {
        log.info("Deleting quality check: {}", id);
        if (!qualityCheckRepository.existsById(id)) {
            throw new QualityCheckNotFoundException(id);
        }
        qualityCheckRepository.deleteById(id);
        log.info("Quality check deleted: {}", id);
    }
}
