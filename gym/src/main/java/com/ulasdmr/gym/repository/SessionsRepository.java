package com.ulasdmr.gym.repository;

import com.ulasdmr.gym.enums.WorkoutType;
import com.ulasdmr.gym.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionsRepository extends JpaRepository<Sessions, Long> {
    List<Sessions> findAllByType(WorkoutType type);
}
