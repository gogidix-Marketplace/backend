package com.gogidix.shared.warehousing.receipt.application.command;

import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReceiptCommand {

    private Receipt.ReceiptStatus status;
    private List<Receipt.ReceiptLine> lines;
    private List<Receipt.ReceiptDocument> documents;
    private String notes;
    private String receivedBy;
    private String dockDoor;
    private String carrier;
    private String vehicleNumber;
}
