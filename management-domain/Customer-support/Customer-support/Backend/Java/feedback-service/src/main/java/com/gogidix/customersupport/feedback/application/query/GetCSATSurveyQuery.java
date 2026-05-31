package com.gogidix.customersupport.feedback.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCSATSurveyQuery {

    private String tenantId;
    private String id;
}
