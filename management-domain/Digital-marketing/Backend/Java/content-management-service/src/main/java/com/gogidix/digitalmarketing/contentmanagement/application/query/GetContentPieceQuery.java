package com.gogidix.digitalmarketing.contentmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetContentPieceQuery {

    private String tenantId;
    private String id;
}
