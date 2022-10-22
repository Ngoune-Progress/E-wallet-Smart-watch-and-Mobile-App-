package com.example.PayMe.controller;

import com.example.PayMe.requestandresponse.FundTransferRequest;
import com.example.PayMe.requestandresponse.FunderTransferByQrCode;
import com.example.PayMe.service.TransactionByQrCodImp;
import com.example.PayMe.service.TransactionServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/transfer")
public class TransactionController {
    private final TransactionServiceImp transactionService;
    private final TransactionByQrCodImp transactionServiceQr;

    @PostMapping("/fund-transfer")
    public ResponseEntity fundTransfer(@RequestBody FundTransferRequest fundTransferRequest){
        log.info("Fund transfer initiated int core bank from {}",fundTransferRequest.toString());
        return  ResponseEntity.ok(transactionService.fundTransfer(fundTransferRequest));
    }
    @PostMapping("/fund-transfer-qr")
    public ResponseEntity fundTransferByQr(@RequestBody FunderTransferByQrCode fundTransferRequest){
        log.info("Fund transfer initiated int core bank from {}",fundTransferRequest.toString());
        return  ResponseEntity.ok(transactionServiceQr.fundTransfer(fundTransferRequest));
    }
}
