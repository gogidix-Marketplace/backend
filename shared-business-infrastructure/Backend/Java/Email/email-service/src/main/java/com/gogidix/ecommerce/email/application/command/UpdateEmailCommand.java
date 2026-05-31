package com.gogidix.ecommerce.email.application.command;

public record UpdateEmailCommand(String tenantId, String id, String name) {
}