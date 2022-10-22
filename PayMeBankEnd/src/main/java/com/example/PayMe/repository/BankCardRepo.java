package com.example.PayMe.repository;

import com.example.PayMe.Entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCardRepo extends JpaRepository<BankCard,Long> {
    public Optional<BankCard> findByNumber(String number);
}
