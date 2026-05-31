package com.gogidix.ecommerce.checkout.application.dto;

public record Address(
    String street, String city, String state, String postalCode, String country
) {}
