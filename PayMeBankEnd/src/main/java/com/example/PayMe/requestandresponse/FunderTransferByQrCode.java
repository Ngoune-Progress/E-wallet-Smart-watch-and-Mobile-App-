package com.example.PayMe.requestandresponse;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class FunderTransferByQrCode {
    private String fromAccount;
    private String toAccountQrCode;
    private Double amount;
}
