package com.example.PayMe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Builder
@Data
public class TransactionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String senderName;
    private String receiverName;
    private String referenceNumber;
    private String ref;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "users_id",nullable = false)
    private UserEntity users;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, Double amount, TransactionType transactionType, String senderName, String receiverName, String referenceNumber, String ref, UserEntity users) {
        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.referenceNumber = referenceNumber;
        this.ref = ref;
        this.users = users;
    }
}
