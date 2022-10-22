package com.example.PayMe.service;

import com.example.PayMe.Entity.UserEntity;
import org.apache.catalina.User;

import java.util.List;

public interface UserService {
    public UserEntity saveUser(UserEntity user);
    public List<UserEntity> listUser();

}
