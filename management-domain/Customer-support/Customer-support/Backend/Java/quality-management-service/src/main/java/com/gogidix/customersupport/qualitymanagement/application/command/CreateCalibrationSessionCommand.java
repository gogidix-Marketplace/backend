package com.gogidix.customersupport.qualitymanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalibrationSessionCommand {

    private String id;
    private String tenantId;
}
