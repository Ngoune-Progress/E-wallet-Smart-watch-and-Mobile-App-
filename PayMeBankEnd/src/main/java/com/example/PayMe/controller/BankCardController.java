package com.example.PayMe.controller;

import com.example.PayMe.Entity.BankCard;
import com.example.PayMe.models.LinkBankCardRequest;
import com.example.PayMe.repository.BankCardRepo;
import com.example.PayMe.service.BankCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/link_bank")
@RequiredArgsConstructor
public class BankCardController {
    private  final BankCardRepo bankCardRepo;
    private final BankCardService bankCardService;
    @GetMapping("/list")
    public ResponseEntity<List<BankCard>> getAll(){
        return ResponseEntity.ok(bankCardRepo.findAll());
    }
    @PostMapping("/addCard")
    public ResponseEntity<BankCard> addCard(@RequestBody LinkBankCardRequest bankCard){
        return  ResponseEntity.ok( bankCardService.linkBank(bankCard));
    }
}