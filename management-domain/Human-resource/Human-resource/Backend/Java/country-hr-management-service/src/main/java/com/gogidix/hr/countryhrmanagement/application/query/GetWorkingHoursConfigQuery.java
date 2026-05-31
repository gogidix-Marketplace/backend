package com.gogidix.hr.countryhrmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWorkingHoursConfigQuery {

    private String tenantId;
    private String id;
}
