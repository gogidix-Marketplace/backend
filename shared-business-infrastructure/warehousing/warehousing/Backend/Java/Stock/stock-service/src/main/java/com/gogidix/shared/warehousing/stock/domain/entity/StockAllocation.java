package com.gogidix.shared.warehousing.stock.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_allocations")
public class StockAllocation {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String orderId;

    private String orderLineId;

    @Indexed
    private String sku;

    private String locationId;

    private Integer quantity;

    private String status;

    private String allocationId;

    private String warehouseId;

    private String batchNumber;

    private LocalDateTime expirationDate;

    private LocalDateTime allocationTime;
}
