package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.domain.entity.Client;
import com.royalcert.royalsys.domain.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public Page<Client> list(@RequestParam UUID orgId, Pageable pageable) {
        return clientRepository.findByOrganizationIdAndActiveTrue(orgId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> get(@PathVariable UUID id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientRepository.save(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable UUID id, @Valid @RequestBody Client updates) {
        return clientRepository.findById(id).map(client -> {
            client.setCompanyName(updates.getCompanyName());
            client.setLegalName(updates.getLegalName());
            client.setIndustrySector(updates.getIndustrySector());
            client.setNaceCode(updates.getNaceCode());
            client.setEaCode(updates.getEaCode());
            client.setEmployeeCount(updates.getEmployeeCount());
            client.setContactName(updates.getContactName());
            client.setContactEmail(updates.getContactEmail());
            client.setContactPhone(updates.getContactPhone());
            client.setCity(updates.getCity());
            client.setCountry(updates.getCountry());
            return ResponseEntity.ok(clientRepository.save(client));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Page<Client> search(@RequestParam UUID orgId, @RequestParam String q, Pageable pageable) {
        return clientRepository.search(orgId, q, pageable);
    }
}
