package com.gogidix.shared.warehousing.receipt.domain.exception;

public class ReceiptNotFoundException extends RuntimeException {

    public ReceiptNotFoundException(String id) {
        super("Receipt not found: " + id);
    }
}
