package com.ulasdmr.gym.repository;

import com.ulasdmr.gym.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUser_Id(Long userId);

    boolean existsByUser_IdAndSessions_Id(Long userId, Long sessionId);
    List<Booking> findAllBySessions_Id(Long sessionId);


    List<Booking> findAllByGym_Id(Long gymId);

    long countBySessions_Id(Long sessionId);
}
