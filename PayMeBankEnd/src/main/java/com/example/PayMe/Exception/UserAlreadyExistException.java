package com.example.PayMe.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(){
        super();
    }
}
