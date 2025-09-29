package com.ulasdmr.gym.dto.booking;

import com.ulasdmr.gym.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private Long id;
    private Long userId;
    private Long sessionId;
    private Long gymId;
    private BookingStatus status;
    private LocalDateTime bookingDate;


}
