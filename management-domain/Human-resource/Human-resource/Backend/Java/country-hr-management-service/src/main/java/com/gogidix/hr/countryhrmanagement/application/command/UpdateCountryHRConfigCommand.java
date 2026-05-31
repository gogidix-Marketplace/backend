package com.gogidix.hr.countryhrmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryHRConfigCommand {

    private String id;
    private String tenantId;
}
