package com.gogidix.hr.employee.shared.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) { super(message); }
    public BusinessRuleException(String rule, String reason) {
        super("Business rule '" + rule + "' violation: " + reason);
    }
}
