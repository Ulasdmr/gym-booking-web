package com.ulasdmr.gym.controller;

import com.ulasdmr.gym.dto.gym.GymCreateDto;
import com.ulasdmr.gym.dto.gym.GymDto;
import com.ulasdmr.gym.service.IGymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gyms")
@RequiredArgsConstructor
public class GymController {

    private final IGymService gymService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GymDto> create(@Valid @RequestBody GymCreateDto dto) {
        GymDto created = gymService.create(dto);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/all")
    public ResponseEntity<List<GymDto>> getAll() {
        return ResponseEntity.ok(gymService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gymService.getById(id));
    }
}
