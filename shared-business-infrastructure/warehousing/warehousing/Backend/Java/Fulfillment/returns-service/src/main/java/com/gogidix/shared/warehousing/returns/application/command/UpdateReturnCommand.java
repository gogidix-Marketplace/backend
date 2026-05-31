package com.gogidix.shared.warehousing.returns.application.command;

import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReturnCommand {

    private Return.ReturnStatus status;
    private Return.RefundType refundType;
    @Valid
    private List<Return.ReturnItem> items;
    private String trackingNumber;
    private String carrier;
    private String customerNotes;
    private String internalNotes;
}
