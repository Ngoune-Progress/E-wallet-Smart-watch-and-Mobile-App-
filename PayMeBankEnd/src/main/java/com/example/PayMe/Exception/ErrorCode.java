package com.example.PayMe.Exception;

import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_WITH_EMAIL_OR_TEL_ALREADY_EXIST("PAYME-0001","User with email or tel already exsit");
    private final String errorCode;
    private  final String errorMessage;

}
