package com.example.PayMe.service;

import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.requestandresponse.FundTransferRequest;
import com.example.PayMe.requestandresponse.FundTransferRespond;

public interface TransactionService {

        public FundTransferRespond fundTransfer(FundTransferRequest request);
        public Boolean verifySenderAmount(FundTransferRequest request,Double userSenderBalance);

}
