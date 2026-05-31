package com.gogidix.shared.warehousing.stock.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationDTO {
    private String reservationId;
    private String orderId;
    private LocalDateTime expiresAt;
    private String status;
}
