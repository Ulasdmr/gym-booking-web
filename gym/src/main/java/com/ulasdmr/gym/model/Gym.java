package com.ulasdmr.gym.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "gym")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long id;

    @Column(name = "gym_name", nullable = false)
    private String gymName;



}
