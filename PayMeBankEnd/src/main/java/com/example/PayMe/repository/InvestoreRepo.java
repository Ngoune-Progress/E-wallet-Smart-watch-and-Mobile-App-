package com.example.PayMe.repository;

import com.example.PayMe.Entity.Investore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestoreRepo extends JpaRepository<Investore,Long> {
}
