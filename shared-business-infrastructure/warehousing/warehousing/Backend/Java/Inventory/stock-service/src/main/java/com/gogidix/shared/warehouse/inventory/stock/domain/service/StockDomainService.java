package com.gogidix.shared.warehouse.inventory.stock.domain.service;

import com.gogidix.shared.warehouse.inventory.stock.domain.entity.StockItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Domain Service for Stock Operations
 */
@Slf4j
@Service
public class StockDomainService {

    /**
     * Check if stock item is at low stock level
     */
    public boolean isLowStock(StockItem stockItem) {
        return stockItem.getQuantity() <= stockItem.getMinThreshold();
    }

    /**
     * Check if stock item is at overstock level
     */
    public boolean isOverstocked(StockItem stockItem) {
        return stockItem.getQuantity() >= stockItem.getMaxThreshold();
    }

    /**
     * Calculate stock turnover rate
     */
    public double calculateTurnoverRate(int currentStock, int avgMonthlySales) {
        if (avgMonthlySales == 0) {
            return 0.0;
        }
        return (double) currentStock / avgMonthlySales;
    }

    /**
     * Calculate days of stock remaining
     */
    public int calculateDaysOfStockRemaining(int currentStock, int dailyUsage) {
        if (dailyUsage == 0) {
            return Integer.MAX_VALUE;
        }
        return currentStock / dailyUsage;
    }

    /**
     * Determine if reorder is needed
     */
    public boolean needsReorder(StockItem stockItem) {
        return stockItem.getQuantity() <= stockItem.getMinThreshold();
    }

    /**
     * Calculate optimal reorder quantity
     */
    public int calculateReorderQuantity(StockItem stockItem, int economicOrderQuantity) {
        int quantityToMaxThreshold = stockItem.getMaxThreshold() - stockItem.getQuantity();
        return Math.max(economicOrderQuantity, quantityToMaxThreshold);
    }
}
