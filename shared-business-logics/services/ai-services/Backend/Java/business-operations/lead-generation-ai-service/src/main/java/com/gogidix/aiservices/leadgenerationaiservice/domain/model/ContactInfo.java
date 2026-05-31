package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadGenerationException;

import java.util.Objects;
import java.util.regex.Pattern;

public class ContactInfo {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String email;
    private final String phone;
    private final String firstName;
    private final String lastName;
    private final String company;
    private final String jobTitle;
    private final String website;
    private final String linkedInUrl;
    private final CompanySize companySize;
    private final String industry;

    private ContactInfo(Builder builder) {
        if (builder.email == null || builder.email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(builder.email).matches()) {
            throw new LeadGenerationException("Invalid email format: " + builder.email);
        }
        this.email = builder.email;
        this.phone = builder.phone;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.company = builder.company;
        this.jobTitle = builder.jobTitle;
        this.website = builder.website;
        this.linkedInUrl = builder.linkedInUrl;
        this.companySize = builder.companySize;
        this.industry = builder.industry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getWebsite() {
        return website;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public CompanySize getCompanySize() {
        return companySize;
    }

    public String getIndustry() {
        return industry;
    }

    public boolean hasCompanyInfo() {
        return company != null && !company.trim().isEmpty();
    }

    public boolean hasPhone() {
        return phone != null && !phone.trim().isEmpty();
    }

    public boolean isDecisionMaker() {
        if (jobTitle == null) {
            return false;
        }
        String title = jobTitle.toLowerCase();
        return title.contains("ceo") || title.contains("cto") ||
                title.contains("director") || title.contains("vp") ||
                title.contains("vice president") || title.contains("manager") ||
                title.contains("head") || title.contains("lead") ||
                title.contains("owner") || title.contains("founder") ||
                title.contains("president");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public static class Builder {
        private String email;
        private String phone;
        private String firstName;
        private String lastName;
        private String company;
        private String jobTitle;
        private String website;
        private String linkedInUrl;
        private CompanySize companySize;
        private String industry;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder linkedInUrl(String linkedInUrl) {
            this.linkedInUrl = linkedInUrl;
            return this;
        }

        public Builder companySize(CompanySize companySize) {
            this.companySize = companySize;
            return this;
        }

        public Builder industry(String industry) {
            this.industry = industry;
            return this;
        }

        public ContactInfo build() {
            return new ContactInfo(this);
        }
    }
}
