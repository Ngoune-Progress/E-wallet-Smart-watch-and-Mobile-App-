package com.example.PayMe.security.token;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    @Column(nullable = false)
    private Boolean status;
}
