package com.gogidix.hr.notification.domain.enums;

/**
 * NotificationType enum for different notification types
 */
public enum NotificationType {
    // HR Notifications
    POLICY_UPDATE,
    POLICY_ACKNOWLEDGMENT_REMINDER,
    POLICY_EXPIRY_WARNING,

    // Employee Notifications
    ONBOARDING_WELCOME,
    ONBOARDING_TASK_REMINDER,
    BENEFIT_ENROLLMENT,
    BENEFIT_RENEWAL,

    // Leave Notifications
    LEAVE_REQUEST_SUBMITTED,
    LEAVE_REQUEST_APPROVED,
    LEAVE_REQUEST_REJECTED,
    LEAVE_BALANCE_LOW,

    // Payroll Notifications
    PAYSLIP_AVAILABLE,
    PAYROLL_PROCESSED,
    PAYROLL_ERROR,

    // Performance Notifications
    PERFORMANCE_REVIEW_DUE,
    PERFORMANCE_REVIEW_SCHEDULED,
    PERFORMANCE_FEEDBACK_REQUEST,
    GOAL_DUE_REMINDER,

    // Training Notifications
    TRAINING_ASSIGNED,
    TRAINING_REMINDER,
    TRAINING_COMPLETION_DUE,
    CERTIFICATION_EXPIRY,

    // Recruitment Notifications
    INTERVIEW_SCHEDULED,
    CANDIDATE_APPLIED,
    OFFER_SENT,
    OFFER_ACCEPTED,
    OFFER_DECLINED,

    // System Notifications
    SYSTEM_MAINTENANCE,
    SYSTEM_UPDATE,
    SECURITY_ALERT,
    PASSWORD_RESET,
    ACCOUNT_LOCKED,

    // General Notifications
    ANNOUNCEMENT,
    REMINDER,
    ALERT,
    WARNING,
    SUCCESS,
    INFO
}
