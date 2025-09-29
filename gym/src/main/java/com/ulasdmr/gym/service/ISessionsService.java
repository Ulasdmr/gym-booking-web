package com.ulasdmr.gym.service;

import com.ulasdmr.gym.dto.sessions.SessionCreateDto;
import com.ulasdmr.gym.dto.sessions.SessionDto;
import com.ulasdmr.gym.enums.WorkoutType;

import java.util.List;

public interface ISessionsService {

    SessionDto create(SessionCreateDto dto);

    SessionDto update(Long id, SessionDto dto);

    void delete(Long id);

    List<SessionDto> listAll();

    SessionDto getById(Long id);

    List<SessionDto> listByType(WorkoutType type);
}
