package com.gogidix.globalbusinessmanagement.countryingestion.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryDataCommand {

    private String id;
    private String tenantId;
}
