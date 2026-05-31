package com.gogidix.shared.warehousing.label.interfaces.rest;

import com.gogidix.shared.warehousing.label.application.command.GenerateLabelCommand;
import com.gogidix.shared.warehousing.label.application.dto.ShippingLabelDTO;
import com.gogidix.shared.warehousing.label.application.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Labels", description = "Shipping label management APIs")
public class LabelController {

    private final LabelService labelService;

    @PostMapping("/generate")
    @Operation(summary = "Generate shipping label")
    public ResponseEntity<ShippingLabelDTO> generateLabel(@Valid @RequestBody GenerateLabelCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.generateLabel(command));
    }

    @GetMapping("/shipment/{shipmentId}")
    @Operation(summary = "Get labels by shipment")
    public ResponseEntity<List<ShippingLabelDTO>> getLabelsByShipment(@PathVariable String shipmentId) {
        return ResponseEntity.ok(labelService.getLabelsByShipment(shipmentId));
    }

    @GetMapping
    @Operation(summary = "Get all labels")
    public ResponseEntity<List<ShippingLabelDTO>> getAllLabels() {
        return ResponseEntity.ok(labelService.getAllLabels());
    }
}
