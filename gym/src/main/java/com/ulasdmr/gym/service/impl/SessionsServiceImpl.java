package com.ulasdmr.gym.service.impl;

import com.ulasdmr.gym.dto.sessions.SessionCreateDto;
import com.ulasdmr.gym.dto.sessions.SessionDto;
import com.ulasdmr.gym.enums.WorkoutType;
import com.ulasdmr.gym.model.Sessions;
import com.ulasdmr.gym.repository.SessionsRepository;
import com.ulasdmr.gym.service.ISessionsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionsServiceImpl implements ISessionsService {

    private final SessionsRepository sessionsRepository;

    @Override
    public SessionDto create(SessionCreateDto dto) {
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Session type is required");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start and end times are required");
        }
        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalStateException("End time must be after start time");
        }

        Sessions session = new Sessions();
        session.setType(dto.getType());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());

        Sessions saved = sessionsRepository.save(session);
        return mapToDto(saved);
    }

    @Override
    public SessionDto update(Long id, SessionDto dto) {
        if (id == null) {
            throw new IllegalArgumentException("Session id is required");
        }
        Sessions entity = sessionsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));


        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getStartTime() != null) {
            entity.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            entity.setEndTime(dto.getEndTime());
        }

        LocalDateTime start = entity.getStartTime();
        LocalDateTime end = entity.getEndTime();
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times are required");
        }
        if (!end.isAfter(start)) {
            throw new IllegalStateException("End time must be after start time");
        }

        Sessions saved = sessionsRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Session id is required");
        }
        if (!sessionsRepository.existsById(id)) {
            throw new EntityNotFoundException("Session not found");
        }
        sessionsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDto> listAll() {
        return sessionsRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDto getById(Long id) {
        Sessions session = sessionsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        return mapToDto(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDto> listByType(WorkoutType type) {
        if (type == null) {
            throw new IllegalArgumentException("Session type is required");
        }

        return sessionsRepository.findAllByType(type)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    private SessionDto mapToDto(Sessions s) {
        SessionDto dto = new SessionDto();
        dto.setId(s.getId());
        dto.setType(s.getType());
        dto.setStartTime(s.getStartTime());
        dto.setEndTime(s.getEndTime());
        return dto;
    }
}
