package com.gogidix.aiservices.aifrauddetectionservice.shared.exception;

import lombok.Getter;

import java.time.Instant;

/**
 * Exception thrown when a fraud detection policy is violated.
 * <p>
 * This exception indicates that a transaction or operation has breached
 * defined fraud detection rules, thresholds, or policies.
 */
@Getter
public class PolicyViolationException extends FraudDetectionException {

    private final String policyId;
    private final String ruleId;
    private final Double fraudScore;
    private final Double threshold;

    /**
     * Constructs a new PolicyViolationException.
     *
     * @param message   the detail message
     * @param policyId  the ID of the violated policy
     * @param ruleId    the ID of the specific rule that was violated
     * @param fraudScore the calculated fraud score
     * @param threshold the threshold that was exceeded
     */
    public PolicyViolationException(String message, String policyId, String ruleId, Double fraudScore, Double threshold) {
        super(ErrorCode.POLICY_VIOLATION, message);
        this.policyId = policyId;
        this.ruleId = ruleId;
        this.fraudScore = fraudScore;
        this.threshold = threshold;
    }

    /**
     * Constructs a new PolicyViolationException with a cause.
     *
     * @param message   the detail message
     * @param policyId  the ID of the violated policy
     * @param ruleId    the ID of the specific rule that was violated
     * @param fraudScore the calculated fraud score
     * @param threshold the threshold that was exceeded
     * @param cause     the root cause
     */
    public PolicyViolationException(String message, String policyId, String ruleId, Double fraudScore, Double threshold, Throwable cause) {
        super(ErrorCode.POLICY_VIOLATION, message, cause);
        this.policyId = policyId;
        this.ruleId = ruleId;
        this.fraudScore = fraudScore;
        this.threshold = threshold;
    }

    /**
     * Constructs a new PolicyViolationException with full specification.
     *
     * @param errorCode the specific error code
     * @param message   the detail message
     * @param policyId  the ID of the violated policy
     * @param ruleId    the ID of the specific rule that was violated
     * @param fraudScore the calculated fraud score
     * @param threshold the threshold that was exceeded
     */
    public PolicyViolationException(ErrorCode errorCode, String message, String policyId, String ruleId, Double fraudScore, Double threshold) {
        super(errorCode, message);
        this.policyId = policyId;
        this.ruleId = ruleId;
        this.fraudScore = fraudScore;
        this.threshold = threshold;
    }

    /**
     * Constructs a new PolicyViolationException with full specification and cause.
     *
     * @param errorCode the specific error code
     * @param message   the detail message
     * @param policyId  the ID of the violated policy
     * @param ruleId    the ID of the specific rule that was violated
     * @param fraudScore the calculated fraud score
     * @param threshold the threshold that was exceeded
     * @param cause     the root cause
     * @param errorId   the unique error ID
     * @param timestamp the timestamp when the error occurred
     */
    public PolicyViolationException(ErrorCode errorCode, String message, String policyId, String ruleId,
                                    Double fraudScore, Double threshold, Throwable cause,
                                    String errorId, Instant timestamp) {
        super(errorCode, message, cause, errorId, timestamp);
        this.policyId = policyId;
        this.ruleId = ruleId;
        this.fraudScore = fraudScore;
        this.threshold = threshold;
    }

    /**
     * Creates a PolicyViolationException for threshold exceeded scenario.
     *
     * @param policyId   the policy ID
     * @param ruleId     the rule ID
     * @param fraudScore the calculated fraud score
     * @param threshold  the threshold that was exceeded
     * @return a new PolicyViolationException instance
     */
    public static PolicyViolationException thresholdExceeded(String policyId, String ruleId, double fraudScore, double threshold) {
        String message = String.format("Fraud score %.2f exceeds threshold %.2f for policy %s, rule %s",
                fraudScore, threshold, policyId, ruleId);
        return new PolicyViolationException(ErrorCode.FRAUD_THRESHOLD_EXCEEDED, message, policyId, ruleId, fraudScore, threshold);
    }

    /**
     * Creates a PolicyViolationException for suspicious pattern detection.
     *
     * @param policyId the policy ID
     * @param ruleId   the rule ID
     * @param pattern  the detected pattern description
     * @return a new PolicyViolationException instance
     */
    public static PolicyViolationException suspiciousPattern(String policyId, String ruleId, String pattern) {
        String message = String.format("Suspicious pattern detected: %s (policy: %s, rule: %s)", pattern, policyId, ruleId);
        return new PolicyViolationException(ErrorCode.SUSPICIOUS_PATTERN_DETECTED, message, policyId, ruleId, null, null);
    }
}
