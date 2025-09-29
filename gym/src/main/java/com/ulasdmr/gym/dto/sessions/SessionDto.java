package com.ulasdmr.gym.dto.sessions;

import com.ulasdmr.gym.enums.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {

    private Long id;
    private WorkoutType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


}
