package com.example.PayMe.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class PostProject  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String personName;
    private String theme;
    private String description;
    private String minInvest;
    private String email;
    private int numLike;
    private  int numOfInvestor;
    private String date;
    @JsonManagedReference
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Investore> investores;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    public PostProject() {
    }

    public PostProject(Long id, String theme, String description, String minInvest, String email, int numLike, int numOfInvestor, String date, UserEntity user) {
        this.id = id;
        this.theme = theme;
        this.description = description;
        this.minInvest = minInvest;
        this.email = email;
        this.numLike = numLike;
        this.numOfInvestor = numOfInvestor;
        this.date = date;
        this.user = user;
    }
}
