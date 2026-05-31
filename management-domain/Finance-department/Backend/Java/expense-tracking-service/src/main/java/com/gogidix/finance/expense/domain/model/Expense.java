package com.gogidix.finance.expense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    private String tenantId;
    private String title;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String category;
    private String department;
    private String submittedBy;
    private String approvedBy;
    private LocalDate expenseDate;
    private String status;
}
