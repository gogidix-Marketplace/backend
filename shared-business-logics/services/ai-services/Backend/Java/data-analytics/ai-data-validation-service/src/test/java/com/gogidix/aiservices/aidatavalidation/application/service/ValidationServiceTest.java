package com.gogidix.aiservices.aidatavalidation.application.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import com.gogidix.aiservices.aidatavalidation.domain.port.out.ValidationRepository;
import com.gogidix.aiservices.aidatavalidation.domain.model.*;
import com.gogidix.aiservices.aidatavalidation.shared.exception.ValidationNotFoundException;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationServiceTest {
    @Mock ValidationRepository repo;
    @InjectMocks ValidationService svc;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    void getStatistics() {
        when(repo.getStatistics()).thenReturn(Map.of("total", 0L));
        assertThat(svc.getStatistics()).isNotNull();
    }

    @Test
    void cancelValidationThrows() {
        assertThatThrownBy(() -> svc.cancelValidation("id"))
            .isInstanceOf(ValidationNotFoundException.class);
    }

    @Test
    void deleteValidationRuleThrows() {
        assertThatThrownBy(() -> svc.deleteValidationRule("r1"))
            .isInstanceOf(ValidationNotFoundException.class);
    }

    @Test
    void setRuleEnabledThrows() {
        assertThatThrownBy(() -> svc.setRuleEnabled("r1", true))
            .isInstanceOf(ValidationNotFoundException.class);
    }

    @Test
    void getValidationResultNotFound() {
        when(repo.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> svc.getValidationResult("id"))
            .isInstanceOf(ValidationNotFoundException.class);
    }

    @Test
    void addValidationRule() {
        svc.addValidationRule("r1", ValidationType.SCHEMA, Map.of());
    }

    @Test
    void getPendingValidations() {
        when(repo.findPending()).thenReturn(Collections.emptyList());
        assertThat(svc.getPendingValidations()).isEmpty();
    }

    @Test
    void getRulesByType() {
        when(repo.findRulesByType(ValidationType.SCHEMA)).thenReturn(Collections.emptyList());
        assertThat(svc.getRulesByType(ValidationType.SCHEMA)).isEmpty();
    }
}
