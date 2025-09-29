package com.ulasdmr.gym.service.impl;

import com.ulasdmr.gym.dto.booking.BookingCreateDto;
import com.ulasdmr.gym.dto.booking.BookingDto;
import com.ulasdmr.gym.enums.BookingStatus;
import com.ulasdmr.gym.enums.UserType;
import com.ulasdmr.gym.model.Booking;
import com.ulasdmr.gym.model.Gym;
import com.ulasdmr.gym.model.Sessions;
import com.ulasdmr.gym.model.User;
import com.ulasdmr.gym.repository.BookingRepository;
import com.ulasdmr.gym.repository.GymRepository;
import com.ulasdmr.gym.repository.SessionsRepository;
import com.ulasdmr.gym.repository.UserRepository;
import com.ulasdmr.gym.service.IBookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionsRepository sessionsRepository;
    private final GymRepository gymRepository;

    @Override
    public BookingDto create(Long currentUserId, BookingCreateDto dto) {
        if (currentUserId == null) {
            throw new IllegalArgumentException("Current user id is required");
        }
        if (dto == null || dto.getSessionId() == null || dto.getGymId() == null) {
            throw new IllegalArgumentException("SessionId and GymId are required");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Sessions session = sessionsRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));

        Gym gym = gymRepository.findById(dto.getGymId())
                .orElseThrow(() -> new EntityNotFoundException("Gym not found"));


        if (bookingRepository.existsByUser_IdAndSessions_Id(user.getId(), session.getId())) {
            throw new IllegalStateException("User already has a booking for this session");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSessions(session);
        booking.setGym(gym);


        Date when = dto.getBookingDate() != null
                ? Date.from(dto.getBookingDate().atZone(ZoneId.systemDefault()).toInstant())
                : new Date();
        booking.setBookingDate(when);

        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);
        return mapToDto(saved);
    }

    @Override
    public void cancel(Long bookingId, Long currentUserId) {
        if (bookingId == null || currentUserId == null) {
            throw new IllegalArgumentException("Booking id and current user id are required");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));


        User current = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isOwner = booking.getUser().getId().equals(current.getId());
        boolean isAdmin = current.getUserType() == UserType.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You are not allowed to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return mapToDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> listMine(Long currentUserId) {
        if (currentUserId == null) {
            throw new IllegalArgumentException("Current user id is required");
        }
        return bookingRepository.findAllByUser_Id(currentUserId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> listBySession(Long sessionId) {
        return bookingRepository.findAllBySessions_Id(sessionId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> listByGym(Long gymId) {
        return bookingRepository.findAllByGym_Id(gymId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countBySession(Long sessionId) {
        return bookingRepository.countBySessions_Id(sessionId);
    }


    private BookingDto mapToDto(Booking b) {
        BookingDto dto = new BookingDto();
        dto.setId(b.getId());
        dto.setUserId(b.getUser().getId());
        dto.setSessionId(b.getSessions().getId());
        dto.setGymId(b.getGym().getId());
        dto.setStatus(b.getStatus());
        dto.setBookingDate(toLocalDateTime(b.getBookingDate()));
        return dto;
    }

    private LocalDateTime toLocalDateTime(Date d) {
        return d == null ? null : LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }
}
