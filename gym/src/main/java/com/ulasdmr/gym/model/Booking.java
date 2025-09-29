package com.ulasdmr.gym.model;

import com.ulasdmr.gym.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint
        (name = "uk_booking_user_session", columnNames = {"user_id","session_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_user"))
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_session"))
    private Sessions sessions;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gym_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_booking_gym"))
    private Gym gym;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date bookingDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private BookingStatus status = BookingStatus.CONFIRMED;
}
