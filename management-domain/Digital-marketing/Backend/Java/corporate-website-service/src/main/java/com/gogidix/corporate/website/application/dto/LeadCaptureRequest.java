package com.gogidix.corporate.website.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadCaptureRequest {
    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phone;

    private String company;

    private String jobTitle;

    private String industry;

    @NotBlank
    private String country;

    private String message;

    @NotBlank
    private String source;

    private String medium;

    private String campaign;

    private String referralUrl;
}
