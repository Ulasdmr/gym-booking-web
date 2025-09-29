package com.ulasdmr.gym.controller;

import com.ulasdmr.gym.dto.sessions.SessionCreateDto;
import com.ulasdmr.gym.dto.sessions.SessionDto;
import com.ulasdmr.gym.service.ISessionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionsController {

    private final ISessionsService sessionsService;


    @PostMapping
    public ResponseEntity<SessionDto> create(@Valid @RequestBody SessionCreateDto dto) {
        return ResponseEntity.status(201).body(sessionsService.create(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SessionDto> update(@PathVariable Long id,
                                             @Valid @RequestBody SessionDto dto) {
        return ResponseEntity.ok(sessionsService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SessionDto>> listAll() {
        return ResponseEntity.ok(sessionsService.listAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SessionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionsService.getById(id));
    }

}
