package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.User;
import com.royalcert.royalsys.domain.entity.WorkflowEvent;
import com.royalcert.royalsys.domain.enums.WorkflowState;
import com.royalcert.royalsys.domain.repository.WorkflowEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowEventRepository eventRepository;

    public WorkflowService(WorkflowEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public WorkflowEvent recordTransition(String entityType, UUID entityId,
                                           WorkflowState fromState, WorkflowState toState,
                                           String eventType, User performedBy, String notes) {
        WorkflowEvent event = new WorkflowEvent();
        event.setEntityType(entityType);
        event.setEntityId(entityId);
        event.setFromState(fromState != null ? fromState.name() : null);
        event.setToState(toState.name());
        event.setEventType(eventType);
        event.setOccurredAt(Instant.now());
        event.setPerformedBy(performedBy);
        event.setNotes(notes);
        return eventRepository.save(event);
    }

    public List<WorkflowEvent> getHistory(String entityType, UUID entityId) {
        return eventRepository.findByEntityTypeAndEntityIdOrderByOccurredAtAsc(entityType, entityId);
    }

    public Optional<Duration> calculateLeadTime(String entityType, UUID entityId,
                                                  String fromState, String toState) {
        Optional<WorkflowEvent> fromEvent = eventRepository.findFirstEventForState(entityType, entityId, fromState);
        Optional<WorkflowEvent> toEvent = eventRepository.findFirstEventForState(entityType, entityId, toState);

        if (fromEvent.isPresent() && toEvent.isPresent()) {
            return Optional.of(Duration.between(fromEvent.get().getOccurredAt(), toEvent.get().getOccurredAt()));
        }
        return Optional.empty();
    }
}
