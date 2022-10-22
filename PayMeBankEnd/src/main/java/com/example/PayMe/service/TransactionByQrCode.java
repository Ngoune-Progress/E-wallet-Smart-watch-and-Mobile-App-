package com.example.PayMe.service;

import com.example.PayMe.requestandresponse.FundTransferRequest;
import com.example.PayMe.requestandresponse.FundTransferRespond;
import com.example.PayMe.requestandresponse.FunderTransferByQrCode;
import com.example.PayMe.requestandresponse.FunderTransferByQrCodeRespond;

public interface TransactionByQrCode {
    public FunderTransferByQrCodeRespond fundTransfer(FunderTransferByQrCode request);
    public Boolean verifySenderAmount(FunderTransferByQrCode request,Double userSenderBalance);
}
