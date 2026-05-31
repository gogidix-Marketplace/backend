package com.gogidix.shared.warehousing.stock.domain.entity;

import com.gogidix.shared.warehousing.stock.application.command.AllocateStockCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_reservations")
public class StockReservation {

    @Id
    private String id;

    private String reservationId;

    @Indexed
    private String tenantId;

    @Indexed
    private String orderId;

    private List<AllocateStockCommand.OrderItem> items;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private ReservationStatus status;

    public enum ReservationStatus {
        ACTIVE, RELEASED, EXPIRED, CONVERTED
    }
}
