package com.gogidix.shared.warehousing.receipt.application.service;

import com.gogidix.shared.warehousing.receipt.application.command.CreateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.command.UpdateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.dto.ReceiptDTO;
import com.gogidix.shared.warehousing.receipt.application.mapper.ReceiptMapper;
import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import com.gogidix.shared.warehousing.receipt.domain.exception.ReceiptNotFoundException;
import com.gogidix.shared.warehousing.receipt.domain.repository.ReceiptRepository;
import com.gogidix.shared.warehousing.receipt.infrastructure.security.TenantContext;
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
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptDTO createReceipt(CreateReceiptCommand command) {
        log.info("Creating receipt for PO: {}", command.getPurchaseOrderNumber());

        String tenantId = TenantContext.getCurrentTenantId();

        Receipt receipt = receiptMapper.toEntity(command);
        receipt.setTenantId(tenantId);
        receipt.setStatus(Receipt.ReceiptStatus.PENDING);
        receipt.setReceiptDate(LocalDateTime.now());
        receipt.setReceiptNumber(generateReceiptNumber());

        Receipt savedReceipt = receiptRepository.save(receipt);

        log.info("Receipt created with number: {}", savedReceipt.getReceiptNumber());
        return receiptMapper.toDTO(savedReceipt);
    }

    @Transactional(readOnly = true)
    public ReceiptDTO getReceipt(String id) {
        Receipt receipt = receiptRepository.findById(id)
            .orElseThrow(() -> new ReceiptNotFoundException(id));
        return receiptMapper.toDTO(receipt);
    }

    @Transactional(readOnly = true)
    public ReceiptDTO getReceiptByNumber(String receiptNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        Receipt receipt = receiptRepository.findByTenantIdAndReceiptNumber(tenantId, receiptNumber)
            .orElseThrow(() -> new ReceiptNotFoundException(receiptNumber));
        return receiptMapper.toDTO(receipt);
    }

    @Transactional(readOnly = true)
    public List<ReceiptDTO> getAllReceipts() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Receipt> receipts = receiptRepository.findByTenantId(tenantId);
        return receiptMapper.toDTOList(receipts);
    }

    @Transactional(readOnly = true)
    public List<ReceiptDTO> getReceiptsByStatus(Receipt.ReceiptStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Receipt> receipts = receiptRepository.findByTenantIdAndStatus(tenantId, status);
        return receiptMapper.toDTOList(receipts);
    }

    public ReceiptDTO updateReceipt(String id, UpdateReceiptCommand command) {
        log.info("Updating receipt: {}", id);

        Receipt receipt = receiptRepository.findById(id)
            .orElseThrow(() -> new ReceiptNotFoundException(id));

        receiptMapper.updateEntity(receipt, command);
        Receipt updatedReceipt = receiptRepository.save(receipt);

        log.info("Receipt updated: {}", id);
        return receiptMapper.toDTO(updatedReceipt);
    }

    public ReceiptDTO processReceipt(String id) {
        log.info("Processing receipt: {}", id);

        Receipt receipt = receiptRepository.findById(id)
            .orElseThrow(() -> new ReceiptNotFoundException(id));

        receipt.setStatus(Receipt.ReceiptStatus.IN_RECEIVING);
        Receipt updatedReceipt = receiptRepository.save(receipt);

        return receiptMapper.toDTO(updatedReceipt);
    }

    public ReceiptDTO completeReceipt(String id) {
        log.info("Completing receipt: {}", id);

        Receipt receipt = receiptRepository.findById(id)
            .orElseThrow(() -> new ReceiptNotFoundException(id));

        receipt.setStatus(Receipt.ReceiptStatus.FULLY_RECEIVED);
        receipt.setActualDeliveryDate(LocalDateTime.now());
        Receipt updatedReceipt = receiptRepository.save(receipt);

        return receiptMapper.toDTO(updatedReceipt);
    }

    public void deleteReceipt(String id) {
        log.info("Deleting receipt: {}", id);
        if (!receiptRepository.existsById(id)) {
            throw new ReceiptNotFoundException(id);
        }
        receiptRepository.deleteById(id);
    }

    private String generateReceiptNumber() {
        return "RCP-" + System.currentTimeMillis();
    }
}
