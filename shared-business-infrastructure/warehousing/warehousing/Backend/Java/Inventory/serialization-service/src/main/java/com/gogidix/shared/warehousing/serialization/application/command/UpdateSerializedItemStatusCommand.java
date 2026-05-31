package com.gogidix.shared.warehousing.serialization.application.command;

import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem.SerializedItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to update serialized item status
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to update serialized item status")
public class UpdateSerializedItemStatusCommand {

    @NotNull(message = "Status is required")
    @Schema(description = "New status", required = true)
    private SerializedItemStatus status;

    @Schema(description = "User making the change")
    private String changedBy;

    @Schema(description = "Notes about the status change")
    private String notes;
}
