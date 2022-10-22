package com.example.PayMe.repository;

import com.example.PayMe.Entity.FriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendsRepo extends JpaRepository<FriendsEntity,Long> {
FriendsEntity findByEmail(String email);
}
