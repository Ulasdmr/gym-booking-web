package com.ulasdmr.gym.service;

import com.ulasdmr.gym.dto.booking.BookingCreateDto;
import com.ulasdmr.gym.dto.booking.BookingDto;

import java.util.List;

public interface IBookingService {

    BookingDto create(Long currentUserId, BookingCreateDto dto);

    void cancel(Long bookingId, Long currentUserId);

    BookingDto getById(Long id);

    List<BookingDto> listMine(Long currentUserId);

    List<BookingDto> listBySession(Long sessionId);

    List<BookingDto> listByGym(Long gymId);

    long countBySession(Long sessionId);


}
