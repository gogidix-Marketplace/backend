package com.gogidix.shared.warehousing.order.interfaces.rest;

import com.gogidix.shared.warehousing.order.application.command.CreateOrderCommand;
import com.gogidix.shared.warehousing.order.application.dto.OutboundOrderDTO;
import com.gogidix.shared.warehousing.order.application.service.OrderService;
import com.gogidix.shared.warehousing.order.domain.entity.OutboundOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outbound-orders")
@RequiredArgsConstructor
@Tag(name = "Outbound Orders", description = "Outbound order management APIs")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create outbound order")
    public ResponseEntity<OutboundOrderDTO> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(command));
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OutboundOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    public ResponseEntity<List<OutboundOrderDTO>> getOrdersByStatus(@PathVariable OutboundOrder.OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OutboundOrderDTO> updateStatus(
            @PathVariable String id,
            @RequestParam OutboundOrder.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
