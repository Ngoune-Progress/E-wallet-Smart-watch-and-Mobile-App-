package com.example.PayMe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class BankCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String number;
    private String ccv;
//    @Column(nullable = false)
//    private String pin;
//    @Column(nullable = false)
    private String cardType;
//    private Double amount;
//    @Column(nullable = false)
//    private String exp;
    @Column(nullable = false)
    private  String name;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
