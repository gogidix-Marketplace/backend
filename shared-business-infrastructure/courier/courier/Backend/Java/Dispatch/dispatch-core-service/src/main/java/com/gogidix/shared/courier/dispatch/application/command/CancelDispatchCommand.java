package com.gogidix.shared.courier.dispatch.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to cancel a dispatch order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelDispatchCommand {

    @NotBlank(message = "Cancelled by is required")
    private String cancelledBy;

    private String cancellationReason;
}
