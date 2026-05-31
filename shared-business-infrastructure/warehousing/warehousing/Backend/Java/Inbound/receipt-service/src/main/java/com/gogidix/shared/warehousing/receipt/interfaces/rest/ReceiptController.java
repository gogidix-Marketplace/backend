package com.gogidix.shared.warehousing.receipt.interfaces.rest;

import com.gogidix.shared.warehousing.receipt.application.command.CreateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.command.UpdateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.dto.ReceiptDTO;
import com.gogidix.shared.warehousing.receipt.application.service.ReceiptService;
import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
@Tag(name = "Receipts", description = "Goods receipt management APIs")
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    @Operation(summary = "Create a new receipt", description = "Creates a new goods receipt")
    public ResponseEntity<ReceiptDTO> createReceipt(@Valid @RequestBody CreateReceiptCommand command) {
        ReceiptDTO receipt = receiptService.createReceipt(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get receipt by ID", description = "Retrieves receipt details")
    public ResponseEntity<ReceiptDTO> getReceipt(@Parameter(description = "Receipt ID") @PathVariable String id) {
        ReceiptDTO receipt = receiptService.getReceipt(id);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/number/{receiptNumber}")
    @Operation(summary = "Get receipt by number", description = "Retrieves receipt by receipt number")
    public ResponseEntity<ReceiptDTO> getReceiptByNumber(
            @Parameter(description = "Receipt number") @PathVariable String receiptNumber) {
        ReceiptDTO receipt = receiptService.getReceiptByNumber(receiptNumber);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping
    @Operation(summary = "Get all receipts", description = "Retrieves all receipts for current tenant")
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        List<ReceiptDTO> receipts = receiptService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get receipts by status", description = "Retrieves receipts by status")
    public ResponseEntity<List<ReceiptDTO>> getReceiptsByStatus(
            @Parameter(description = "Status") @PathVariable Receipt.ReceiptStatus status) {
        List<ReceiptDTO> receipts = receiptService.getReceiptsByStatus(status);
        return ResponseEntity.ok(receipts);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update receipt", description = "Updates receipt details")
    public ResponseEntity<ReceiptDTO> updateReceipt(
            @Parameter(description = "Receipt ID") @PathVariable String id,
            @Valid @RequestBody UpdateReceiptCommand command) {
        ReceiptDTO receipt = receiptService.updateReceipt(id, command);
        return ResponseEntity.ok(receipt);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "Process receipt", description = "Starts processing a receipt")
    public ResponseEntity<ReceiptDTO> processReceipt(@Parameter(description = "Receipt ID") @PathVariable String id) {
        ReceiptDTO receipt = receiptService.processReceipt(id);
        return ResponseEntity.ok(receipt);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete receipt", description = "Marks a receipt as complete")
    public ResponseEntity<ReceiptDTO> completeReceipt(@Parameter(description = "Receipt ID") @PathVariable String id) {
        ReceiptDTO receipt = receiptService.completeReceipt(id);
        return ResponseEntity.ok(receipt);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete receipt", description = "Deletes a receipt")
    public ResponseEntity<Void> deleteReceipt(@Parameter(description = "Receipt ID") @PathVariable String id) {
        receiptService.deleteReceipt(id);
        return ResponseEntity.noContent().build();
    }
}
