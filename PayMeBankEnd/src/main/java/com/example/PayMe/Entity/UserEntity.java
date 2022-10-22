package com.example.PayMe.Entity;

import com.example.PayMe.security.token.ConfirmationToken;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;



@Entity
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long usersId;
    private String firstName;
    private String lastName;

    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @Column(unique = true,nullable = false)
    private String accountNumber;
    @Column(unique = true,nullable = false,length = 9)
    private String tel;
    @Column(nullable = false)
    private Boolean locked;
    private Boolean enabled;
    @Column(nullable = false)
    private Double balance;

    private Double investmentBalance;
    @Column(nullable = false,length = 5)
    private String pin;
    @Column(nullable = false)
    private String qrCode;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    private List<FriendsEntity> friends;
    @JsonManagedReference
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<TransactionEntity> transactions;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<CashOutEntity> cashOuts;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<ConfirmationToken> confirmationTokens;
    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<PostProject> postProjects;
    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<BankCard> bankCards;
    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Investore> investores;



    public UserEntity(String firstName, String lastName, String email, String password,String tel,String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.pin=pin;

    }

}






