package com.gogidix.digitalmarketing.budgetmanagement.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequestDto {
    private String tenantId;
    private String name;
     private String fiscalYear;
     private String totalAmount;
     private String allocatedAmount;
     private String committedAmount;
     private String spentAmount;
     private String status;
     private String currency;
     private String budgetCategory;
     private String country;
     private String department;

}