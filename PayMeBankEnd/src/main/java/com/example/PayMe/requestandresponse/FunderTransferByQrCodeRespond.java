package com.example.PayMe.requestandresponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FunderTransferByQrCodeRespond {
    private String message;
    private String transactionId;
}
