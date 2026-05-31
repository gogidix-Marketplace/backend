package com.gogidix.shared.warehousing.putaway.application.command;

import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePutawayTaskCommand {

    @NotBlank
    private String receiptId;

    private String receiptLineNumber;

    @NotBlank
    private String sku;

    private String productName;

    @NotNull
    private Integer quantity;

    private String fromLocation;

    private Integer priority;
}
