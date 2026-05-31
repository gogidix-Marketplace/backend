package com.gogidix.customersupport.livechat.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChatSessionQuery {

    private String tenantId;
    private String id;
}
