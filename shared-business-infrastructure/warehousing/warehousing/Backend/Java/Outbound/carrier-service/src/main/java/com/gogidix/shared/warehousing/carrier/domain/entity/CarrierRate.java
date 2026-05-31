package com.gogidix.shared.warehousing.carrier.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carrier_rates")
@CompoundIndex(def = "{'tenantId': 1, 'carrierCode': 1, 'serviceCode': 1}", name = "idx_tenant_carrier_service")
@Schema(description = "Carrier rate entity")
public class CarrierRate {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String carrierCode;

    private String serviceCode;

    private String originZone;

    private String destinationZone;

    private BigDecimal baseRate;

    private BigDecimal ratePerKg;

    private BigDecimal ratePerCm3;

    private String currency;

    private LocalDateTime effectiveDate;

    private LocalDateTime expiryDate;

    private Boolean active;
}
