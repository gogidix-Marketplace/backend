package com.gogidix.ecommerce.email.application.query;

public record GetEmailListQuery(String tenantId, int page, int size) {
}