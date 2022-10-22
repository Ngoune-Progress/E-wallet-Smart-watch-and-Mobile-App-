package com.example.PayMe.requestandresponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UtilityPaymentRespond {
    private String message;
    private String transactionId;

}
