package com.gogidix.sales.communication.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommunicationChannelQuery {

    private String tenantId;
    private String id;
}
