package com.example.PayMe.repository;

import com.example.PayMe.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByQrCode(String qrcode);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByTel(String tel);
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a SET a.enabled=true WHERE a.email=?1")
    int enableAppUser(String email);
    @Override
    @Query("SELECT um FROM UserEntity um")
    List<UserEntity> findAll();
}
