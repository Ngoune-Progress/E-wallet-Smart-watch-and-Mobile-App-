package com.example.PayMe.service;

import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.QRCodeGenerator;
import com.example.PayMe.repository.UserRepository;
import com.example.PayMe.security.PasswordEncode;
import com.example.PayMe.security.token.ConfirmationToken;
import com.example.PayMe.security.token.ConfirmationTokenService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

@Service
public class AppUserService  {

    private final UserRepository appUserRepository;
    private final PasswordEncode passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public AppUserService(UserRepository appUserRepository, PasswordEncode passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return (UserDetails) appUserRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
//    }
//

    public String signUpUser(UserEntity appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        boolean userExists1 = appUserRepository.findByTel(appUser.getTel()).isPresent();


        if (userExists && userExists1) {

            UserEntity appUserPrevious =  appUserRepository.findByEmail(appUser.getEmail()).get();
            Boolean isEnabled = appUserPrevious.getEnabled();

            if (!isEnabled) {
                String token = UUID.randomUUID().toString();

                //A method to save user and token in this class
                saveConfirmationToken(appUserPrevious, token);

                return token;

            }
            throw new IllegalStateException(String.format("User with email %s already exists!", appUser.getEmail()));
        }

        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        //Save User QR CODE

    String medium=UUID.randomUUID().toString();
    String date = LocalDateTime.now().toString();
//    String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        medium = "PayMe"+medium+date;
        String aN= genAccountNum();
                appUser.setAccountNumber(aN);

                appUser.setEnabled(false);
                appUser.setLocked(false);
                appUser.setQrCode(medium);
                appUser.setBalance(0.00);


        //Saving the user after encoding the password
        appUserRepository.save(appUser);

        //Creating a token from UUID
        String token = UUID.randomUUID().toString();

        //Getting the confirmation token and then saving it
        saveConfirmationToken(appUser, token);


        //Returning token
        return token;
    }


    private void saveConfirmationToken(UserEntity appUser, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);

    }

    public String genAccountNum(){

        String start = "5R";
        Random value = new Random();

        //Generate two values to append to 'BE'
        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        start += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for(int i =0; i < 12;i++)
        {
            if(count == 4)
            {
                start += " ";
                count =0;
            }
            else
                n = value.nextInt(10);
            start += Integer.toString(n);
            count++;

        }
        return start;
    }
}
