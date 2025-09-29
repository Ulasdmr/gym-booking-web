package com.ulasdmr.gym.model;

import com.ulasdmr.gym.enums.WorkoutType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkoutType type;


    @Column(name = "start_at", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endTime;

}
