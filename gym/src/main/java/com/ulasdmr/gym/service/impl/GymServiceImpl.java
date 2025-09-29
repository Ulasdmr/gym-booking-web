package com.ulasdmr.gym.service.impl;

import com.ulasdmr.gym.dto.gym.GymCreateDto;
import com.ulasdmr.gym.dto.gym.GymDto;
import com.ulasdmr.gym.model.Gym;
import com.ulasdmr.gym.repository.GymRepository;
import com.ulasdmr.gym.service.IGymService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GymServiceImpl implements IGymService {

    private final GymRepository gymRepository;

    @Override
    public GymDto create(GymCreateDto dto) {
        Gym gym = new Gym();
        gym.setGymName(dto.getGymName());

        Gym saved = gymRepository.save(gym);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymDto> getAll() {
        return gymRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GymDto getById(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gym not found"));
        return mapToDto(gym);
    }

    private GymDto mapToDto(Gym gym) {
        GymDto dto = new GymDto();
        dto.setGymId(gym.getId());
        dto.setGymName(gym.getGymName());
        return dto;
    }
}
