package com.example.PayMe.service;

import com.example.PayMe.Entity.Investore;
import com.example.PayMe.Entity.PostProject;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.repository.InvestoreRepo;
import com.example.PayMe.repository.ProjectRepo;
import com.example.PayMe.repository.UserRepository;
import com.example.PayMe.requestandresponse.FundTransferRequest;
import com.example.PayMe.requestandresponse.FundTransferRespond;
import com.example.PayMe.requestandresponse.FunderTransferByQrCode;
import com.example.PayMe.requestandresponse.InvestmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestoreServiceImp implements InvestoreService{
    private final InvestoreRepo investoreRepo;
    private final UserRepository userRepository;
    private final ProjectRepo projectRepo;

    @Override
    public void  changeFromUserAccountToInvestbalanceAccount(InvestmentRequest investmentRequest){
        Optional<UserEntity> userSender = userRepository.findByEmail(investmentRequest.getSender());
        Optional<UserEntity> userReceiver = userRepository.findByEmail(investmentRequest.getReceiver());
     PostProject postId = projectRepo.findById(investmentRequest.getPostId()).get();

        UserEntity userEntitySender = userSender.get();
        UserEntity userEntityReceiver = userReceiver.get();
        if(verifySenderAmount(investmentRequest,userSender.get().getBalance())){
            Double uSender = userSender.get().getBalance();
            Double uSenderInvestor = userSender.get().getInvestmentBalance();
            Double uReceiver = userReceiver.get().getBalance();
            uSender = uSender - investmentRequest.getAmount();
            uSenderInvestor = uSenderInvestor + investmentRequest.getAmount();

            userEntitySender.setBalance(uSender);
            userEntitySender.setInvestmentBalance(uSenderInvestor);
            Investore.builder().amount(investmentRequest.getAmount())
            .post(postId)
            .phone(userEntitySender.getTel())
            .user(userEntitySender)
            .build();
            userRepository.save(userEntitySender);
//            userEntityReceiver.setPostProjects();

        }


    }

    @Override
    public Boolean verifySenderAmount(InvestmentRequest request, Double userSenderBalance) {
        Double userSender = userSenderBalance;
        Double requestAmount = request.getAmount();
        if (requestAmount > 0) {
            if (userSender < requestAmount) {
                return false;
            } else {
                return true;
            }
        }else {
            return  false;
        }    }
}
