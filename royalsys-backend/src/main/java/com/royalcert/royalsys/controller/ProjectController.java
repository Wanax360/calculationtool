package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.domain.entity.Project;
import com.royalcert.royalsys.domain.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public Page<Project> list(@RequestParam UUID orgId, Pageable pageable) {
        return projectRepository.findByOrganizationId(orgId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable UUID id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.ok(projectRepository.save(project));
    }
}
