package com.gogidix.shared.warehousing.order.application.service;

import com.gogidix.shared.warehousing.order.application.command.CreateOrderCommand;
import com.gogidix.shared.warehousing.order.application.dto.OutboundOrderDTO;
import com.gogidix.shared.warehousing.order.application.mapper.OrderMapper;
import com.gogidix.shared.warehousing.order.domain.entity.OutboundOrder;
import com.gogidix.shared.warehousing.order.domain.repository.OutboundOrderRepository;
import com.gogidix.shared.warehousing.order.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OutboundOrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OutboundOrderDTO createOrder(CreateOrderCommand command) {
        String tenantId = TenantContext.getCurrentTenantId();
        OutboundOrder order = orderMapper.toEntity(command);
        order.setTenantId(tenantId);
        order.setOrderNumber("OB-" + System.currentTimeMillis());
        order.setStatus(OutboundOrder.OrderStatus.PENDING);
        return orderMapper.toDTO(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OutboundOrderDTO> getAllOrders() {
        String tenantId = TenantContext.getCurrentTenantId();
        return orderMapper.toDTOList(orderRepository.findByTenantId(tenantId));
    }

    @Transactional(readOnly = true)
    public List<OutboundOrderDTO> getOrdersByStatus(OutboundOrder.OrderStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        return orderMapper.toDTOList(orderRepository.findByTenantIdAndStatus(tenantId, status));
    }

    public OutboundOrderDTO updateStatus(String id, OutboundOrder.OrderStatus status) {
        OutboundOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        order.setStatus(status);

        if (status == OutboundOrder.OrderStatus.SHIPPED) {
            order.setActualShipDate(LocalDateTime.now());
        }

        return orderMapper.toDTO(orderRepository.save(order));
    }
}
