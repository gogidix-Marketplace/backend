package com.gogidix.shared.audit.adapter.out.persistence;

import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.AuditSearchCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditEventRepositoryAdapterTest {

    @Mock
    private AuditEventJpaRepository jpaRepository;

    @InjectMocks
    private AuditEventRepositoryAdapter adapter;

    @Test
    void save_delegatesToJpaRepository() {
        AuditEvent event = AuditEvent.builder().eventId("evt-1").userId("user1").build();
        when(jpaRepository.save(event)).thenReturn(event);

        AuditEvent result = adapter.save(event);

        assertNotNull(result);
        assertEquals("evt-1", result.getEventId());
        verify(jpaRepository).save(event);
    }

    @Test
    void findById_found() {
        AuditEvent event = AuditEvent.builder().eventId("evt-1").build();
        when(jpaRepository.findById("evt-1")).thenReturn(Optional.of(event));

        Optional<AuditEvent> result = adapter.findById("evt-1");

        assertTrue(result.isPresent());
        assertEquals("evt-1", result.get().getEventId());
    }

    @Test
    void findById_notFound() {
        when(jpaRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<AuditEvent> result = adapter.findById("missing");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByCriteria_returnsAllEvents() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("user1").build();
        AuditEvent event1 = AuditEvent.builder().eventId("evt-1").build();
        AuditEvent event2 = AuditEvent.builder().eventId("evt-2").build();
        when(jpaRepository.findAll()).thenReturn(List.of(event1, event2));

        List<AuditEvent> results = adapter.findByCriteria(criteria);

        assertEquals(2, results.size());
        verify(jpaRepository).findAll();
    }

    @Test
    void findByCriteria_emptyResult() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<AuditEvent> results = adapter.findByCriteria(criteria);

        assertTrue(results.isEmpty());
    }

    @Test
    void countAll() {
        when(jpaRepository.count()).thenReturn(42L);

        long result = adapter.countAll();

        assertEquals(42L, result);
        verify(jpaRepository).count();
    }

    @Test
    void deleteOldEvents() {
        int retentionDays = 90;

        adapter.deleteOldEvents(retentionDays);

        verify(jpaRepository).deleteByTimestampBefore(any(LocalDateTime.class));
    }

    @Test
    void deleteOldEvents_withDifferentRetention() {
        adapter.deleteOldEvents(365);

        verify(jpaRepository).deleteByTimestampBefore(argThat(cutoff ->
                cutoff.isBefore(LocalDateTime.now())
        ));
    }
}