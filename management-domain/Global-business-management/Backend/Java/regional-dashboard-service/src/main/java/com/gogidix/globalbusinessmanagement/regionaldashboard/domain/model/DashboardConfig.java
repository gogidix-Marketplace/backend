package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardConfig {
    private String theme;
    private String layout;
    private Integer refreshInterval;
    private Boolean autoRefresh;
    private String defaultPeriod;
    private Map<String, Object> filters;
    private Map<String, Object> settings;
}
