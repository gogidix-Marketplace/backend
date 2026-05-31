package com.gogidix.shared.warehousing.returns.application.service;

import com.gogidix.shared.warehousing.returns.application.command.CreateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.command.UpdateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.dto.ReturnDTO;
import com.gogidix.shared.warehousing.returns.application.mapper.ReturnMapper;
import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import com.gogidix.shared.warehousing.returns.domain.events.ReturnCreatedEvent;
import com.gogidix.shared.warehousing.returns.domain.events.ReturnUpdatedEvent;
import com.gogidix.shared.warehousing.returns.domain.exception.ReturnNotFoundException;
import com.gogidix.shared.warehousing.returns.domain.repository.ReturnRepository;
import com.gogidix.shared.warehousing.returns.infrastructure.messaging.ReturnEventPublisher;
import com.gogidix.shared.warehousing.returns.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final ReturnMapper returnMapper;
    private final ReturnEventPublisher eventPublisher;

    public ReturnDTO createReturn(CreateReturnCommand command) {
        log.info("Creating return for order: {}", command.getOrderNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        Return ret = returnMapper.toEntity(command);
        ret.setTenantId(tenantId);
        ret.setStatus(Return.ReturnStatus.REQUESTED);
        ret.setRequestedDate(LocalDateTime.now());
        ret.setRmaNumber(generateRmaNumber());
        ret.setCurrency("USD");

        BigDecimal refundAmount = calculateRefundAmount(command.getItems());
        ret.setRefundAmount(refundAmount);

        Return savedReturn = returnRepository.save(ret);

        ReturnCreatedEvent event = ReturnCreatedEvent.builder()
            .returnId(savedReturn.getId())
            .rmaNumber(savedReturn.getRmaNumber())
            .orderNumber(savedReturn.getOrderNumber())
            .customerId(savedReturn.getCustomerId())
            .reason(savedReturn.getReason().name())
            .refundAmount(savedReturn.getRefundAmount().doubleValue())
            .tenantId(savedReturn.getTenantId())
            .build();
        eventPublisher.publishReturnCreated(event);

        log.info("Return created with RMA: {}", savedReturn.getRmaNumber());
        return returnMapper.toDTO(savedReturn);
    }

    @Transactional(readOnly = true)
    public ReturnDTO getReturn(String id) {
        Return ret = returnRepository.findById(id)
            .orElseThrow(() -> new ReturnNotFoundException(id));
        return returnMapper.toDTO(ret);
    }

    @Transactional(readOnly = true)
    public ReturnDTO getReturnByRma(String rmaNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        Return ret = returnRepository.findByTenantIdAndRmaNumber(tenantId, rmaNumber)
            .orElseThrow(() -> new ReturnNotFoundException(rmaNumber, tenantId));
        return returnMapper.toDTO(ret);
    }

    @Transactional(readOnly = true)
    public List<ReturnDTO> getAllReturns() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Return> returns = returnRepository.findByTenantId(tenantId);
        return returnMapper.toDTOList(returns);
    }

    @Transactional(readOnly = true)
    public List<ReturnDTO> getReturnsByOrderNumber(String orderNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Return> returns = returnRepository.findByTenantIdAndOrderNumber(tenantId, orderNumber);
        return returnMapper.toDTOList(returns);
    }

    @Transactional(readOnly = true)
    public List<ReturnDTO> getReturnsByStatus(Return.ReturnStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Return> returns = returnRepository.findByTenantIdAndStatus(tenantId, status);
        return returnMapper.toDTOList(returns);
    }

    public ReturnDTO updateReturn(String id, UpdateReturnCommand command) {
        log.info("Updating return: {}", id);

        String tenantId = TenantContext.getCurrentTenantId();
        Return ret = returnRepository.findById(id)
            .orElseThrow(() -> new ReturnNotFoundException(id));

        returnMapper.updateEntity(ret, command);
        Return updatedReturn = returnRepository.save(ret);

        ReturnUpdatedEvent event = ReturnUpdatedEvent.builder()
            .returnId(updatedReturn.getId())
            .rmaNumber(updatedReturn.getRmaNumber())
            .status(updatedReturn.getStatus().name())
            .changeType("UPDATE")
            .tenantId(updatedReturn.getTenantId())
            .build();
        eventPublisher.publishReturnUpdated(event);

        log.info("Return updated: {}", id);
        return returnMapper.toDTO(updatedReturn);
    }

    public ReturnDTO updateReturnStatus(String id, Return.ReturnStatus status) {
        log.info("Updating return status: {} to {}", id, status);

        String tenantId = TenantContext.getCurrentTenantId();
        Return ret = returnRepository.findById(id)
            .orElseThrow(() -> new ReturnNotFoundException(id));

        ret.setStatus(status);

        if (status == Return.ReturnStatus.RECEIVED) {
            ret.setReceivedDate(LocalDateTime.now());
        } else if (status == Return.ReturnStatus.REFUNDED) {
            ret.setRefundedDate(LocalDateTime.now());
        }

        Return updatedReturn = returnRepository.save(ret);

        ReturnUpdatedEvent event = ReturnUpdatedEvent.builder()
            .returnId(updatedReturn.getId())
            .rmaNumber(updatedReturn.getRmaNumber())
            .status(status.name())
            .changeType("STATUS_UPDATE")
            .tenantId(updatedReturn.getTenantId())
            .build();
        eventPublisher.publishReturnUpdated(event);

        log.info("Return status updated: {} to {}", id, status);
        return returnMapper.toDTO(updatedReturn);
    }

    public ReturnDTO processReturn(String id) {
        log.info("Processing return: {}", id);
        Return ret = returnRepository.findById(id)
            .orElseThrow(() -> new ReturnNotFoundException(id));

        if (ret.getStatus() != Return.ReturnStatus.RECEIVED) {
            throw new IllegalStateException("Return must be received before processing");
        }

        ret.setStatus(Return.ReturnStatus.INSPECTING);
        Return updatedReturn = returnRepository.save(ret);

        return returnMapper.toDTO(updatedReturn);
    }

    public void deleteReturn(String id) {
        log.info("Deleting return: {}", id);
        if (!returnRepository.existsById(id)) {
            throw new ReturnNotFoundException(id);
        }
        returnRepository.deleteById(id);
        log.info("Return deleted: {}", id);
    }

    private String generateRmaNumber() {
        return "RMA-" + System.currentTimeMillis();
    }

    private BigDecimal calculateRefundAmount(List<Return.ReturnItem> items) {
        return items.stream()
            .map(item -> item.getRefundAmount() != null ? item.getRefundAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
