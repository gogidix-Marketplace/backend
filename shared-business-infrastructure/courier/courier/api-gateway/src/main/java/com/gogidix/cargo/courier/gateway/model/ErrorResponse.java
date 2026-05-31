package com.gogidix.cargo.courier.gateway.model;

public record ErrorResponse(String error, String message, int status) {}