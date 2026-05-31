package com.gogidix.transaction.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDashboard {

    private Long openAlerts;
    private Long criticalAlerts;
    private Long warningAlerts;
    private Long recentAlerts;
    private LocalDateTime timestamp;
}
