package com.gogidix.customersupport.phonesupport.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPhoneCallQuery {

    private String tenantId;
    private String id;
}
