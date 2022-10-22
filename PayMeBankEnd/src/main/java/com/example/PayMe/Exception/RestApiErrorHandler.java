package com.example.PayMe.Exception;

import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@RestControllerAdvice
public class RestApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Error> handleUserAlreadyExistException(){
    Error error =ErrorUtils.createError(ErrorCode.USER_WITH_EMAIL_OR_TEL_ALREADY_EXIST.getErrorMessage(),ErrorCode.USER_WITH_EMAIL_OR_TEL_ALREADY_EXIST.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.setUrl("/api/v1/user/addUser");
            error.setReqMethod((HttpMethod.POST).toString());
        return  new ResponseEntity(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
