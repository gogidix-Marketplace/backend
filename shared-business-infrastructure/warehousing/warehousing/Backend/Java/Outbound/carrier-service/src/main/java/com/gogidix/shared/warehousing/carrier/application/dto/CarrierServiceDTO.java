package com.gogidix.shared.warehousing.carrier.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrierServiceDTO {
    private String serviceCode;
    private String serviceName;
    private String serviceType;
    private Boolean available;
}
