package com.gogidix.hr.countryhrmanagement.shared.base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.LastModifiedBy;
import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    protected String id;
    @CreatedDate
    protected Instant createdAt;
    @LastModifiedDate
    protected Instant updatedAt;
    @CreatedBy
    @LastModifiedBy
    protected String updatedBy;
}
