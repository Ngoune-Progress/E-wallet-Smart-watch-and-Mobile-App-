package com.example.PayMe.requestandresponse;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundTransferRequest {
    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String ref;
}
