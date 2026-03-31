package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.domain.entity.WorkflowEvent;
import com.royalcert.royalsys.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/history/{entityType}/{entityId}")
    public ResponseEntity<List<WorkflowEvent>> getHistory(
            @PathVariable String entityType, @PathVariable UUID entityId) {
        return ResponseEntity.ok(workflowService.getHistory(entityType, entityId));
    }
}
