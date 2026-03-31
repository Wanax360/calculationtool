package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.WorkflowEvent;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import com.royalcert.royalsys.domain.repository.WorkflowEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    @Mock private WorkflowEventRepository eventRepository;
    @InjectMocks private WorkflowService service;

    @Test
    void testRecordTransition() {
        UUID entityId = UUID.randomUUID();
        when(eventRepository.save(any())).thenAnswer(inv -> {
            WorkflowEvent e = inv.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        WorkflowEvent event = service.recordTransition("PROJECT", entityId,
                WorkflowState.PROJECT_CREATED, WorkflowState.PROJECT_ACTIVE,
                "PROJECT_ACTIVATED", null, "Activated");

        assertNotNull(event);
        assertEquals("PROJECT", event.getEntityType());
        assertEquals("PROJECT_ACTIVE", event.getToState());

        ArgumentCaptor<WorkflowEvent> captor = ArgumentCaptor.forClass(WorkflowEvent.class);
        verify(eventRepository).save(captor.capture());
        assertEquals(entityId, captor.getValue().getEntityId());
    }

    @Test
    void testCalculateLeadTime() {
        UUID entityId = UUID.randomUUID();
        Instant from = Instant.parse("2024-01-01T00:00:00Z");
        Instant to = Instant.parse("2024-01-04T12:00:00Z");

        WorkflowEvent fromEvent = new WorkflowEvent();
        fromEvent.setOccurredAt(from);

        WorkflowEvent toEvent = new WorkflowEvent();
        toEvent.setOccurredAt(to);

        when(eventRepository.findFirstEventForState("PROJECT", entityId, "PROJECT_CREATED"))
                .thenReturn(Optional.of(fromEvent));
        when(eventRepository.findFirstEventForState("PROJECT", entityId, "PROJECT_ACTIVE"))
                .thenReturn(Optional.of(toEvent));

        Optional<Duration> duration = service.calculateLeadTime("PROJECT", entityId,
                "PROJECT_CREATED", "PROJECT_ACTIVE");

        assertTrue(duration.isPresent());
        assertEquals(3, duration.get().toDays());
        assertEquals(12, duration.get().toHoursPart());
    }

    @Test
    void testCalculateLeadTimeMissingEvent() {
        UUID entityId = UUID.randomUUID();
        when(eventRepository.findFirstEventForState(any(), any(), any())).thenReturn(Optional.empty());

        Optional<Duration> duration = service.calculateLeadTime("PROJECT", entityId, "A", "B");
        assertTrue(duration.isEmpty());
    }
}
