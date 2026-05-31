package com.gogidix.management.executive.approval.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetApprovalQuery {

    private String tenantId;
    private String id;
}
