package com.example.PayMe.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
public class FriendsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lName;
    private String accountNumber;
    private String email;
    private String tel;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    public FriendsEntity() {
    }

    public FriendsEntity(Long id, String name, String lName, String accountNumber, String email, String tel, UserEntity user) {
        this.id = id;
        this.name = name;
        this.lName = lName;
        this.accountNumber = accountNumber;
        this.email = email;
        this.tel = tel;
        this.user = user;
    }
}
