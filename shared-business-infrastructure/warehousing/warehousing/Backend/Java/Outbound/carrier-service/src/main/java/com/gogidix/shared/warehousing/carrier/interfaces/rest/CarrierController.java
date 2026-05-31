package com.gogidix.shared.warehousing.carrier.interfaces.rest;

import com.gogidix.shared.warehousing.carrier.application.dto.CarrierDTO;
import com.gogidix.shared.warehousing.carrier.application.dto.CarrierRateDTO;
import com.gogidix.shared.warehousing.carrier.application.service.CarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Carriers", description = "Carrier management APIs")
public class CarrierController {

    private final CarrierService carrierService;

    @GetMapping
    @Operation(summary = "Get all active carriers")
    public ResponseEntity<List<CarrierDTO>> getActiveCarriers() {
        return ResponseEntity.ok(carrierService.getActiveCarriers());
    }

    @GetMapping("/{carrierCode}/rates")
    @Operation(summary = "Get carrier rates")
    public ResponseEntity<List<CarrierRateDTO>> getRates(
            @PathVariable String carrierCode,
            @RequestParam String originZone,
            @RequestParam String destinationZone) {
        return ResponseEntity.ok(carrierService.getRates(carrierCode, originZone, destinationZone));
    }

    @PostMapping("/select")
    @Operation(summary = "Select best carrier")
    public ResponseEntity<CarrierDTO> selectBestCarrier(
            @RequestParam String originZone,
            @RequestParam String destinationZone,
            @RequestParam BigDecimal weight,
            @RequestParam String serviceType) {
        return ResponseEntity.ok(carrierService.selectBestCarrier(originZone, destinationZone, weight, serviceType));
    }
}
