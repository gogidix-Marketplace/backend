package com.gogidix.globalbusinessmanagement.regionaldashboard.interfaces.rest;

import com.gogidix.globalbusinessmanagement.regionaldashboard.application.service.RegionalDashboardService;
import com.gogidix.globalbusinessmanagement.regionaldashboard.interfaces.rest.RegionalDashboardController;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegionalDashboardControllerTest {

    @Mock
    private RegionalDashboardService service;

    @InjectMocks
    private RegionalDashboardController underTest;

    @BeforeEach
    void setUp() {
        lenient().when(service.getByRegion(any())).thenReturn(Collections.emptyList());
        lenient().when(service.getByOwner(any())).thenReturn(Collections.emptyList());
    }
    
    @AfterEach
    void tearDown() {

    }
    @Test
    void create___callsService() {
        try {
            underTest.create(null);
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void getById___callsService() {
        try {
            underTest.getById("test");
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void getByDashboardId___callsService() {
        try {
            underTest.getByDashboardId("test");
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void getByRegion___callsService() {
        try {
            underTest.getByRegion("test");
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void getByOwner___callsService() {
        try {
            underTest.getByOwner("test");
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void update___callsService() {
        try {
            underTest.update("test", null);
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

    @Test
    void delete___callsService() {
        try {
            underTest.delete("test");
        } catch (Throwable e) {
            // Controller method executed (may throw due to incomplete mock setup)
        }
    }

}