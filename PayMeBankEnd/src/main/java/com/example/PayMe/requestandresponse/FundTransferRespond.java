package com.example.PayMe.requestandresponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FundTransferRespond {
    private String message;
    private String transactionId;
}
