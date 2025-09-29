package com.ulasdmr.gym.controller;

import com.ulasdmr.gym.dto.booking.BookingCreateDto;
import com.ulasdmr.gym.dto.booking.BookingDto;
import com.ulasdmr.gym.jwt.JwtService;
import com.ulasdmr.gym.service.IBookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class  BookingController {

    private final IBookingService bookingService;
    private final JwtService jwtService;


    @PostMapping
    public ResponseEntity<BookingDto> create(@Valid @RequestBody BookingCreateDto dto,
                                             HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return ResponseEntity.ok(bookingService.create(currentUserId, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        bookingService.cancel(id, currentUserId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }


    @GetMapping("/me")
    public ResponseEntity<List<BookingDto>> listMine(HttpServletRequest request) {
        Long currentUserId = currentUserId(request);
        return ResponseEntity.ok(bookingService.listMine(currentUserId));
    }


    @GetMapping("/by-session/{sessionId}")
    public ResponseEntity<List<BookingDto>> listBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(bookingService.listBySession(sessionId));
    }


    @GetMapping("/by-gym/{gymId}")
    public ResponseEntity<List<BookingDto>> listByGym(@PathVariable Long gymId) {
        return ResponseEntity.ok(bookingService.listByGym(gymId));
    }


    @GetMapping("/count")
    public ResponseEntity<Long> countBySession(@RequestParam Long sessionId) {
        return ResponseEntity.ok(bookingService.countBySession(sessionId));
    }


    private Long currentUserId(HttpServletRequest request) {
        final String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new IllegalStateException("Authorization header missing");
        }
        final String token = auth.substring(7);
        Long uid = jwtService.getUserIdByToken(token);
        if (uid == null) {
            throw new IllegalStateException("Invalid token: user id not found");
        }
        return uid;
    }
}
