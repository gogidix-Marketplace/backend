package com.gogidix.customersupport.phonesupport.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePhoneCallCommand {

    private String id;
    private String tenantId;
}
