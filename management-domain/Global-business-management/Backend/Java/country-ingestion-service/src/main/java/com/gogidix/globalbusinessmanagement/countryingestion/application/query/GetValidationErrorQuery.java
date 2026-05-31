package com.gogidix.globalbusinessmanagement.countryingestion.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetValidationErrorQuery {

    private String tenantId;
    private String id;
}
