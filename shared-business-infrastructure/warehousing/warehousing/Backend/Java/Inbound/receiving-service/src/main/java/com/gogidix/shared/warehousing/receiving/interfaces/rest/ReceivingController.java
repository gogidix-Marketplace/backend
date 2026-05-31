package com.gogidix.shared.warehousing.receiving.interfaces.rest;

import com.gogidix.shared.warehousing.receiving.application.service.ReceivingService;
import com.gogidix.shared.warehousing.receiving.domain.entity.ReceivingOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receiving/orders")
@RequiredArgsConstructor
@Tag(name = "Receiving Orders", description = "Receiving order management APIs")
public class ReceivingController {

	private final ReceivingService receivingService;

	@PostMapping
	@Operation(summary = "Create receiving order")
	public ResponseEntity<ReceivingOrder> createOrder(@RequestBody ReceivingOrder order,
			@RequestHeader(value = "X-Tenant-ID", required = false) String tenantId) {
		if (tenantId != null) {
			order.setTenantId(tenantId);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(receivingService.createOrder(order));
	}

	@GetMapping
	@Operation(summary = "Get all orders")
	public ResponseEntity<List<ReceivingOrder>> getAllOrders(
			@RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId) {
		return ResponseEntity.ok(receivingService.getAllOrders(tenantId));
	}

	@GetMapping("/status/{status}")
	@Operation(summary = "Get orders by status")
	public ResponseEntity<List<ReceivingOrder>> getOrdersByStatus(
			@PathVariable ReceivingOrder.ReceivingStatus status,
			@RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId) {
		return ResponseEntity.ok(receivingService.getOrdersByStatus(tenantId, status));
	}

	@PatchMapping("/{id}/status")
	@Operation(summary = "Update order status")
	public ResponseEntity<ReceivingOrder> updateStatus(
			@PathVariable String id,
			@RequestParam ReceivingOrder.ReceivingStatus status) {
		return ResponseEntity.ok(receivingService.updateStatus(id, status));
	}
}
