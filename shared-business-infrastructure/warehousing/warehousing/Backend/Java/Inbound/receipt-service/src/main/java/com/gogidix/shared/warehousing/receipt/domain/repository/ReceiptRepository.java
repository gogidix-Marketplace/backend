package com.gogidix.shared.warehousing.receipt.domain.repository;

import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, String> {

    List<Receipt> findByTenantId(String tenantId);

    Optional<Receipt> findByTenantIdAndReceiptNumber(String tenantId, String receiptNumber);

    List<Receipt> findByTenantIdAndStatus(String tenantId, Receipt.ReceiptStatus status);

    List<Receipt> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    List<Receipt> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<Receipt> findByTenantIdAndReceiptDateBetween(
        String tenantId, LocalDateTime start, LocalDateTime end);
}
