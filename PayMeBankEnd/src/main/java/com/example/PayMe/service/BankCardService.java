package com.example.PayMe.service;

import com.example.PayMe.Entity.BankCard;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.models.LinkBankCardRequest;
import com.example.PayMe.repository.BankCardRepo;
import com.example.PayMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankCardService {
    private final BankCardRepo bankCardRepo;
    private  final UserRepository userRepository;
    public BankCard linkBank(LinkBankCardRequest linkBankCard){

            UserEntity userEntity = userRepository.findByEmail(linkBankCard.getEmail()).get();

          BankCard bankCard = new BankCard();
            switch (linkBankCard.getName()){
               case "SOCIETE GENERALE DE BANQUES AU CAMEROUN" :
                   bankCard.setName("SGBC");
                   break;

                   case "AFRILAND FIRST BANQUES":
                    bankCard.setName("AFRI");
                    break;

                case "UNITED BANK OF AFRICA":
                    bankCard.setName("UBA");
                    break;


            }
            bankCard.setCardType(linkBankCard.getCardType());
            bankCard.setCcv(linkBankCard.getCcv());
//            bankCard.setExp(linkBankCard.getBankCard().getExp());
            bankCard.setNumber(linkBankCard.getNumber());
            bankCard.setUser(userEntity);

            bankCardRepo.save(bankCard);
            return bankCard;

    }
}
