package com.example.PayMe.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Data
public class Error {
    // customizing timestamp serialization format
  private String errorCode;
  private String message;
  private Integer status;
  private String url;
  private String reqMethod;




}
