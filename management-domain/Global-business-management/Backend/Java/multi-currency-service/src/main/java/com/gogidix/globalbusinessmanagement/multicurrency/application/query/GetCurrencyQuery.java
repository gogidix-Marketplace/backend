package com.gogidix.globalbusinessmanagement.multicurrency.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCurrencyQuery {

    private String tenantId;
    private String id;
}
