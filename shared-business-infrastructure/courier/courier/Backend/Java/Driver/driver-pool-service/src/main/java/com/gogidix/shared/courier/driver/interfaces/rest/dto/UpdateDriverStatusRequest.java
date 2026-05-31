package com.gogidix.shared.courier.driver.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverStatusRequest {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ONLINE|OFFLINE|BUSY|ON_BREAK", message = "Status must be ONLINE, OFFLINE, BUSY, or ON_BREAK")
    private String status;
}
