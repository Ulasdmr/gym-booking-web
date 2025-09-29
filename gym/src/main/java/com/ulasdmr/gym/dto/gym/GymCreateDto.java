package com.ulasdmr.gym.dto.gym;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GymCreateDto {

    @NotBlank(message = "Gym name is required")
    private String gymName;
}
