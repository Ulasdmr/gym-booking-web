package com.ulasdmr.gym.service;

import com.ulasdmr.gym.dto.gym.GymCreateDto;
import com.ulasdmr.gym.dto.gym.GymDto;

import java.util.List;

public interface IGymService {

    GymDto create(GymCreateDto dto);

    List<GymDto> getAll();

    GymDto getById(Long id);
}
