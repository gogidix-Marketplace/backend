package com.gogidix.shared.warehousing.carrier.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrierDTO {
    private String id;
    private String tenantId;
    private String carrierCode;
    private String carrierName;
    private String carrierType;
    private Boolean active;
    private String contactEmail;
    private String contactPhone;
    private String apiUrl;
    private String accountNumber;
    private List<CarrierServiceDTO> services;
}
