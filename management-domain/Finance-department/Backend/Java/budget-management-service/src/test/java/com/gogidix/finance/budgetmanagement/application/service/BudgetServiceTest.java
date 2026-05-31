package com.gogidix.finance.budgetmanagement.application.service;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import com.gogidix.finance.budgetmanagement.domain.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {
    @Mock private BudgetRepository repository;
    @InjectMocks private BudgetService service;
    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = Budget.builder().tenantId("t1").build();
    }
    @Test void create_shouldSave() { when(repository.save(any(Budget.class))).thenReturn(budget); assertThat(service.create(budget)).isNotNull(); verify(repository).save(budget); }
    @Test void getById_shouldReturn() { when(repository.findById(any())).thenReturn(Optional.of(budget)); assertThat(service.getById("any")).isNotNull(); }
    @Test void getById_shouldReturnNull() { when(repository.findById("x")).thenReturn(Optional.empty()); assertThat(service.getById("x")).isNull(); }
    @Test void getAll_shouldReturnList() { when(repository.findAll()).thenReturn(Arrays.asList(budget)); assertThat(service.getAll()).hasSize(1); }
    @Test void getByTenantId_shouldReturnList() { when(repository.findByTenantId("t1")).thenReturn(Arrays.asList(budget)); assertThat(service.getByTenantId("t1")).hasSize(1); }
    @Test void update_shouldSave() { when(repository.save(any(Budget.class))).thenReturn(budget); assertThat(service.update(budget)).isNotNull(); }
    @Test void delete_shouldCall() { service.delete("b1"); verify(repository).deleteById("b1"); }
}
