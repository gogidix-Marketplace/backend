package com.gogidix.analytics.bi.domain.port.out;

import com.gogidix.analytics.bi.domain.model.ReportExecution;

/**
 * Output port: Gateway for report notifications.
 */
public interface ReportNotificationGateway {

    void sendReportReadyNotification(ReportExecution execution);

    void sendReportFailedNotification(ReportExecution execution, String errorMessage);
}
