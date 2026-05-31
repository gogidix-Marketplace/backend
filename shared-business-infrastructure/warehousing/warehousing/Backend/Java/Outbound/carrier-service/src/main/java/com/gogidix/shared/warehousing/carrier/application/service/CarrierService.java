package com.gogidix.shared.warehousing.carrier.application.service;

import com.gogidix.shared.warehousing.carrier.application.dto.CarrierDTO;
import com.gogidix.shared.warehousing.carrier.application.dto.CarrierRateDTO;
import com.gogidix.shared.warehousing.carrier.application.mapper.CarrierMapper;
import com.gogidix.shared.warehousing.carrier.domain.entity.Carrier;
import com.gogidix.shared.warehousing.carrier.domain.repository.CarrierRepository;
import com.gogidix.shared.warehousing.carrier.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CarrierService {

    private final CarrierRepository carrierRepository;
    private final CarrierMapper carrierMapper;

    @Transactional(readOnly = true)
    public List<CarrierDTO> getActiveCarriers() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Carrier> carriers = carrierRepository.findByTenantIdAndActive(tenantId, true);
        return carrierMapper.toDTOList(carriers);
    }

    @Transactional(readOnly = true)
    public List<CarrierRateDTO> getRates(String carrierCode, String originZone, String destinationZone) {
        String tenantId = TenantContext.getCurrentTenantId();
        Carrier carrier = carrierRepository.findByTenantIdAndCarrierCode(tenantId, carrierCode)
            .orElseThrow(() -> new IllegalArgumentException("Carrier not found: " + carrierCode));

return carrier.getServices().stream()
.map(service -> CarrierRateDTO.builder()
.carrierId(carrier.getId())
.carrierCode(carrierCode)
.serviceCode(service.getServiceCode())
.serviceName(service.getServiceName())
.baseRate(BigDecimal.valueOf(10.00))
.ratePerKg(BigDecimal.valueOf(0.50))
.currency("USD")
.active(true)
.build())
.map(dto -> (CarrierRateDTO) dto)
.toList();
    }

    public CarrierDTO selectBestCarrier(String originZone, String destinationZone, BigDecimal weight, String serviceType) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<Carrier> carriers = carrierRepository.findByTenantIdAndActive(tenantId, true);

        if (carriers.isEmpty()) {
            throw new IllegalArgumentException("No active carriers found");
        }

        return carrierMapper.toDTO(carriers.get(0));
    }
}
