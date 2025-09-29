package com.ulasdmr.gym.model;

import com.ulasdmr.gym.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserType userType;


    private LocalDateTime birthDate;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

}
