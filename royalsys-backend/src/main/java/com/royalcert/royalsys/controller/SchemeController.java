package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.domain.entity.Scheme;
import com.royalcert.royalsys.domain.repository.SchemeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schemes")
public class SchemeController {

    private final SchemeRepository schemeRepository;

    public SchemeController(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    @GetMapping
    public List<Scheme> list() {
        return schemeRepository.findByActiveTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> get(@PathVariable UUID id) {
        return schemeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
