package com.example.PayMe.service;

import com.example.PayMe.Entity.FriendsEntity;
import com.example.PayMe.Entity.TransactionEntity;
import com.example.PayMe.Entity.TransactionType;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.repository.FriendsRepo;
import com.example.PayMe.repository.TransactionRepo;
import com.example.PayMe.repository.UserRepository;
import com.example.PayMe.requestandresponse.FundTransferRequest;
import com.example.PayMe.requestandresponse.FundTransferRespond;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImp implements TransactionService{
    private final UserRepository userRepository;
    private final TransactionRepo transactionRepo;
    private final FriendsRepo friendsRepo;

    public TransactionServiceImp(UserRepository userRepository, TransactionRepo transactionRepo, FriendsRepo friendsRepo) {
        this.userRepository = userRepository;
        this.transactionRepo = transactionRepo;
        this.friendsRepo = friendsRepo;
    }




    @Override
    public FundTransferRespond fundTransfer(FundTransferRequest request) {

        Optional<UserEntity> userSenderAccount = userRepository.findByEmail(request.getFromAccount());
        Optional<UserEntity> userReceiverAccount = userRepository.findByEmail(request.getToAccount());
        Boolean checkBalance = verifySenderAmount(request,userSenderAccount.get().getBalance());
        UserEntity userEntitySender = userSenderAccount.get();
        UserEntity userEntityReceiver = userReceiverAccount.get();
        String transactionReference = UUID.randomUUID().toString();
        if(checkBalance){
            Double uSender = userSenderAccount.get().getBalance();
            Double uReceiver = userReceiverAccount.get().getBalance();
            uSender = uSender- request.getAmount();
            uReceiver = uReceiver + request.getAmount();
            Double amount = -(request.getAmount());
            userEntitySender.setBalance(uSender);
            userEntityReceiver.setBalance(uReceiver);
            userRepository.save(userEntitySender);
FriendsEntity friendsEntity = friendsRepo.findByEmail(userEntityReceiver.getEmail());
if (friendsEntity!=null) {
    if (friendsEntity.getUser() != userEntitySender) {
        friendsRepo.save(FriendsEntity.builder().accountNumber(userEntityReceiver.getAccountNumber())
                .email(userEntityReceiver.getEmail())
                .tel(userEntityReceiver.getTel())
                .name(userEntityReceiver.getFirstName()).lName(userEntityReceiver.getLastName())
                .user(userEntitySender)
                .build());
    }
}else {
        friendsRepo.save(FriendsEntity.builder().accountNumber(userEntityReceiver.getAccountNumber())
                .email(userEntityReceiver.getEmail())
                .tel(userEntityReceiver.getTel())
                .name(userEntityReceiver.getFirstName()).lName(userEntityReceiver.getLastName())
                .user(userEntitySender)
                .build());
    }


            transactionRepo.save(TransactionEntity.builder().transactionType(TransactionType.FUND_TRANSFER)
            .amount(amount).referenceNumber(userEntitySender.getAccountNumber()).users(userEntitySender)
                    .ref(request.getRef())
                    .senderName(userEntitySender.getFirstName())
                    .receiverName(userEntityReceiver.getFirstName())
                    .build());



            userRepository.save(userEntityReceiver);
            transactionRepo.save(TransactionEntity.builder().transactionType(TransactionType.FUND_TRANSFER)
                    .ref(request.getRef())
                    .senderName(userEntitySender.getFirstName())
                    .receiverName(userEntityReceiver.getFirstName())
                    .amount(request.getAmount()).referenceNumber(userEntityReceiver.getAccountNumber()).users(userEntityReceiver)
                    .build());

                return  FundTransferRespond.builder()
                        .transactionId(transactionReference)
                        .message("Fund Transfer Successfully").build();

        }else {
            return  FundTransferRespond.builder()
                    .transactionId(transactionReference)
                    .message("Fail To Transfer").build();
        }
    }

    @Override
    public Boolean verifySenderAmount(FundTransferRequest request,Double userSenderBalance) {
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
        }
    }
}
