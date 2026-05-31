package com.gogidix.shared.warehousing.receiving.application.service;

import com.gogidix.shared.warehousing.receiving.domain.entity.ReceivingOrder;
import com.gogidix.shared.warehousing.receiving.domain.repository.ReceivingOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceivingService {

	private final ReceivingOrderRepository orderRepository;

	public ReceivingOrder createOrder(ReceivingOrder order) {
		order.setOrderNumber("RO-" + System.currentTimeMillis());
		order.setStatus(ReceivingOrder.ReceivingStatus.SCHEDULED);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());
		return orderRepository.save(order);
	}

	public List<ReceivingOrder> getAllOrders(String tenantId) {
		return orderRepository.findByTenantId(tenantId);
	}

	public List<ReceivingOrder> getOrdersByStatus(String tenantId, ReceivingOrder.ReceivingStatus status) {
		return orderRepository.findByTenantIdAndStatus(tenantId, status);
	}

	public ReceivingOrder updateStatus(String id, ReceivingOrder.ReceivingStatus status) {
		ReceivingOrder order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		order.setStatus(status);

		if (status == ReceivingOrder.ReceivingStatus.ARRIVED) {
			order.setArrivedDate(LocalDateTime.now());
		} else if (status == ReceivingOrder.ReceivingStatus.COMPLETED) {
			order.setCompletedDate(LocalDateTime.now());
		}
		order.setUpdatedAt(LocalDateTime.now());

		return orderRepository.save(order);
	}
}
