package com.gogidix.shared.warehousing.returns.application.command;

import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import com.gogidix.shared.warehousing.returns.domain.entity.Return.ReturnItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReturnCommand {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Return reason is required")
    private Return.ReturnReason reason;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<ReturnItem> items;

    @NotNull(message = "Refund type is required")
    private Return.RefundType refundType;

    private String customerNotes;
    private String trackingNumber;
    private String carrier;
}
