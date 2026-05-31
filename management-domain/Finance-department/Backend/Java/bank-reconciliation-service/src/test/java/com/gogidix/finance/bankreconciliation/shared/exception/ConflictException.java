package com.gogidix.finance.bankreconciliation.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
    public ConflictException(String resource, String id) { super(resource + " with id '" + id + "' already exists"); }
}
