package com.gogidix.hr.employee.shared.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String resource, String id) { super(resource + " with id '" + id + "' not found"); }
}
