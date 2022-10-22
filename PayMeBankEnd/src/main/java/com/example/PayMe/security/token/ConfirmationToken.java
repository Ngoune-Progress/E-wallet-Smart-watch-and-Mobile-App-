package com.example.PayMe.security.token;

import com.example.PayMe.Entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    private UserEntity user;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expireAt, UserEntity user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expireAt;
        this.user = user;
    }
}
