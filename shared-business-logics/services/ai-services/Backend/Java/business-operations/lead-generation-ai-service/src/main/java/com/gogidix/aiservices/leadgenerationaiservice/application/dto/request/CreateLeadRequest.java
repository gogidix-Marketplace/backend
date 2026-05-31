package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.CompanySize;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeadRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private String jobTitle;
    private String website;
    private String linkedInUrl;
    private CompanySize companySize;
    private String industry;
    private String source;
    private LeadChannel channel;
    private String campaignId;
}
