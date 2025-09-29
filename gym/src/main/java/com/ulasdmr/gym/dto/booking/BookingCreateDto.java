package com.ulasdmr.gym.dto.booking;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCreateDto {

    private Long sessionId;
    private Long gymId;

    private LocalDateTime bookingDate;

}
