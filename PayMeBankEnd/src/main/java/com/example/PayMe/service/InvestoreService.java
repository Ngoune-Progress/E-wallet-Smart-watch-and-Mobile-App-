package com.example.PayMe.service;

import com.example.PayMe.requestandresponse.FunderTransferByQrCode;
import com.example.PayMe.requestandresponse.InvestmentRequest;

public interface InvestoreService {
    void  changeFromUserAccountToInvestbalanceAccount(InvestmentRequest investmentRequest);

    Boolean verifySenderAmount(InvestmentRequest request, Double userSenderBalance);
}
