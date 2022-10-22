package com.example.PayMe.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final OTPRepo otpRepo;
    public void saveConfirmationToken(OTP token) {
        otpRepo.save(token);
    }

    public Optional<OTP> getToken(String token) {
        return otpRepo.findByToken(token);
    }

}
