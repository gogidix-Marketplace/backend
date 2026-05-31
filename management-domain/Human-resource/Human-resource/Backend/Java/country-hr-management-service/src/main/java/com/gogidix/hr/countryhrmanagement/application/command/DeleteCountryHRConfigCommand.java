package com.gogidix.hr.countryhrmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCountryHRConfigCommand {

    private String id;
    private String tenantId;
}
