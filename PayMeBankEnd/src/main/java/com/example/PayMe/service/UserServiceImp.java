package com.example.PayMe.service;

import com.example.PayMe.QRCodeGenerator;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.repository.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserEntity saveUser(UserEntity user) {
        String medium=UUID.randomUUID().toString();
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        medium = "PayMe"+medium+date;
        user.setAccountNumber(UUID.randomUUID().toString());

//        byte[] image = new byte[0];
//        try {
//
//            // Generate and Return Qr Code in Byte Array
//            image = QRCodeGenerator.getQRCodeImage(medium,100,100);
//
//        } catch (WriterException | IOException e) {
//            e.printStackTrace();
//        }

//        user.setQrCode(image);
        user.setBalance(0.00);

        userRepository.save(user);
        return  user;
    }

    @Override
    public List<UserEntity> listUser() {
        return  userRepository.findAll();
    }
}
