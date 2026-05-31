package com.gogidix.cargo.ecommerce.gateway.model;

public record ErrorResponse(String error, String message, int status) {}