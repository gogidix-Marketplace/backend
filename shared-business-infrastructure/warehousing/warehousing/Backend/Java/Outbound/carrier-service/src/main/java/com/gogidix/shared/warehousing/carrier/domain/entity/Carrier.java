package com.gogidix.shared.warehousing.carrier.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carriers")
@CompoundIndex(def = "{'tenantId': 1, 'carrierCode': 1}", name = "idx_tenant_carrier")
@Schema(description = "Carrier entity")
public class Carrier {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String carrierCode;

    private String carrierName;

    private CarrierType carrierType;

    private Boolean active;

    private String contactEmail;

    private String contactPhone;

    private String apiUrl;

    private String accountNumber;

    private List<CarrierService> services;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum CarrierType {
        PARCEL, FREIGHT, COURIER, POSTAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarrierService {
        private String serviceCode;
        private String serviceName;
        private ServiceType serviceType;
        private Boolean available;

        public enum ServiceType {
            STANDARD, EXPRESS, OVERNIGHT, SAME_DAY, ECONOMY, FREIGHT
        }
    }
}
