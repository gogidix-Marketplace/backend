package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing marketing leads.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leads")
public class Lead {

    @Id
    private String id;

    @Indexed
    private LeadStatus status;

    private String firstName;

    private String lastName;

    @Indexed
    private String email;

    private String phone;

    private String company;

    private String jobTitle;

    private String industry;

    private String companySize;

    private String country;

    private String source;

    private String medium;

    private String campaign;

    private String contentId;

    private String formId;

    private String leadMagnet;

    @Indexed
    private String assignedTo;

    private String assignedToName;

    private String notes;

    private Map<String, Object> customFields;

    private Integer score;

    @Indexed
    private String tenantId;

    @Indexed
    private LocalDateTime convertedAt;

    private String convertedBy;

    private String customerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return firstName != null ? firstName : email;
    }

    public boolean isConverted() {
        return status == LeadStatus.WON;
    }

    public void updateScore(Integer delta) {
        this.score = (this.score == null) ? delta : this.score + delta;
    }
}
