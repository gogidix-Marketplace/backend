package com.gogidix.aiservices.aidatavalidation.application;

import org.junit.jupiter.api.*;
import org.mockito.*;
import com.gogidix.aiservices.aidatavalidation.application.port.out.ValidationResultRepository;
import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceAdapter;
import com.gogidix.aiservices.aidatavalidation.domain.DataQualityService;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppValidationServiceTest {
    @Mock ValidationResultRepository resultRepo;
    @Mock DataSourceAdapter dataSourceAdapter;
    @Mock DataQualityService qualityService;
    @InjectMocks ValidationService svc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValidationResult() {
        when(resultRepo.findById("id")).thenReturn(Optional.empty());
        var result = svc.getValidationResult("id");
        assertThat(result).isEmpty();
    }

    @Test
    void applyCustomRules() {
        assertThat(svc.applyCustomRules(List.of("rule1", "rule2"))).isTrue();
    }

    @Test
    void applyCustomRulesEmpty() {
        svc.applyCustomRules(Collections.emptyList());
    }
}
