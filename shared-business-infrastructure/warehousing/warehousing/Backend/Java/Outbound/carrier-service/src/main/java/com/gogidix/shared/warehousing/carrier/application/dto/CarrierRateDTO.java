package com.gogidix.shared.warehousing.carrier.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrierRateDTO {
    private String id;
    private String carrierId;
    private String carrierCode;
    private String serviceCode;
    private String serviceName;
    private String originZone;
    private String destinationZone;
    private BigDecimal baseRate;
    private BigDecimal perKgRate;
    private BigDecimal ratePerKg;
    private BigDecimal perCubicMeterRate;
    private Integer minDays;
    private Integer maxDays;
    private Boolean active;
    private String currency;
}
