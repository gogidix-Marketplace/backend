package com.gogidix.shared.warehousing.returns.interfaces.rest;

import com.gogidix.shared.warehousing.returns.application.command.CreateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.command.UpdateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.dto.ReturnDTO;
import com.gogidix.shared.warehousing.returns.application.service.ReturnService;
import com.gogidix.shared.warehousing.returns.domain.entity.Return;
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
@RequestMapping("/returns")
@RequiredArgsConstructor
@Tag(name = "Returns", description = "Returns processing APIs")
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping
    @Operation(summary = "Create a new return", description = "Creates a new return request")
    public ResponseEntity<ReturnDTO> createReturn(@Valid @RequestBody CreateReturnCommand command) {
        ReturnDTO ret = returnService.createReturn(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ret);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get return by ID", description = "Retrieves return details by ID")
    public ResponseEntity<ReturnDTO> getReturn(@Parameter(description = "Return ID") @PathVariable String id) {
        ReturnDTO ret = returnService.getReturn(id);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/rma/{rmaNumber}")
    @Operation(summary = "Get return by RMA number", description = "Retrieves return details by RMA number")
    public ResponseEntity<ReturnDTO> getReturnByRma(
            @Parameter(description = "RMA number") @PathVariable String rmaNumber) {
        ReturnDTO ret = returnService.getReturnByRma(rmaNumber);
        return ResponseEntity.ok(ret);
    }

    @GetMapping
    @Operation(summary = "Get all returns", description = "Retrieves all returns for current tenant")
    public ResponseEntity<List<ReturnDTO>> getAllReturns() {
        List<ReturnDTO> returns = returnService.getAllReturns();
        return ResponseEntity.ok(returns);
    }

    @GetMapping("/order/{orderNumber}")
    @Operation(summary = "Get returns by order number", description = "Retrieves all returns for a specific order")
    public ResponseEntity<List<ReturnDTO>> getReturnsByOrderNumber(
            @Parameter(description = "Order number") @PathVariable String orderNumber) {
        List<ReturnDTO> returns = returnService.getReturnsByOrderNumber(orderNumber);
        return ResponseEntity.ok(returns);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get returns by status", description = "Retrieves all returns with a specific status")
    public ResponseEntity<List<ReturnDTO>> getReturnsByStatus(
            @Parameter(description = "Return status") @PathVariable Return.ReturnStatus status) {
        List<ReturnDTO> returns = returnService.getReturnsByStatus(status);
        return ResponseEntity.ok(returns);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update return", description = "Updates return details")
    public ResponseEntity<ReturnDTO> updateReturn(
            @Parameter(description = "Return ID") @PathVariable String id,
            @Valid @RequestBody UpdateReturnCommand command) {
        ReturnDTO ret = returnService.updateReturn(id, command);
        return ResponseEntity.ok(ret);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update return status", description = "Updates the status of a return")
    public ResponseEntity<ReturnDTO> updateReturnStatus(
            @Parameter(description = "Return ID") @PathVariable String id,
            @Parameter(description = "New status") @RequestParam Return.ReturnStatus status) {
        ReturnDTO ret = returnService.updateReturnStatus(id, status);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "Process return", description = "Starts processing a received return")
    public ResponseEntity<ReturnDTO> processReturn(@Parameter(description = "Return ID") @PathVariable String id) {
        ReturnDTO ret = returnService.processReturn(id);
        return ResponseEntity.ok(ret);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete return", description = "Deletes a return")
    public ResponseEntity<Void> deleteReturn(@Parameter(description = "Return ID") @PathVariable String id) {
        returnService.deleteReturn(id);
        return ResponseEntity.noContent().build();
    }
}
