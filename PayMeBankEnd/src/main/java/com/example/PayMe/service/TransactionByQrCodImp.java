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
import com.example.PayMe.requestandresponse.FunderTransferByQrCode;
import com.example.PayMe.requestandresponse.FunderTransferByQrCodeRespond;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service

public class TransactionByQrCodImp implements TransactionByQrCode {
    private final UserRepository userRepository;
    private final TransactionRepo transactionRepo;
    private final FriendsRepo friendsRepo;

    public TransactionByQrCodImp(UserRepository userRepository, TransactionRepo transactionRepo, FriendsRepo friendsRepo) {
        this.userRepository = userRepository;
        this.transactionRepo = transactionRepo;
        this.friendsRepo = friendsRepo;
    }

    @Override
    public FunderTransferByQrCodeRespond fundTransfer(FunderTransferByQrCode request) {
        Optional<UserEntity> userSenderAccount = userRepository.findByEmail(request.getFromAccount());
        Optional<UserEntity> userReceiverAccount = userRepository.findByQrCode(request.getToAccountQrCode());
        Boolean checkBalance = verifySenderAmount(request, userSenderAccount.get().getBalance());
        UserEntity userEntitySender = userSenderAccount.get();
        UserEntity userEntityReceiver = userReceiverAccount.get();
        String transactionReference = UUID.randomUUID().toString();
        if (checkBalance) {
            Double uSender = userSenderAccount.get().getBalance();
            Double uReceiver = userReceiverAccount.get().getBalance();
            uSender = uSender - request.getAmount();
            uReceiver = uReceiver + request.getAmount();
            Double amount = -(request.getAmount());
//            String medium=UUID.randomUUID().toString();
//            String date = LocalDateTime.now().toString();
//             medium = "PayMe"+medium+date;
//             userEntityReceiver.setQrCode(medium);

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
                    .referenceNumber(transactionReference)
                    .senderName(userEntitySender.getFirstName())
                    .receiverName(userEntityReceiver.getFirstName())
                    .build());



            userRepository.save(userEntityReceiver);
            transactionRepo.save(TransactionEntity.builder().transactionType(TransactionType.FUND_TRANSFER)
                    .referenceNumber(transactionReference)
                    .amount(request.getAmount()).referenceNumber(userEntityReceiver.getAccountNumber()).users(userEntityReceiver)
                    .build());

            return  FunderTransferByQrCodeRespond.builder()
                    .transactionId(transactionReference)
                    .message("Fund Transfer Successfully").build();

        }else {
            return  FunderTransferByQrCodeRespond.builder()
                    .transactionId(transactionReference)
                    .message("Fail To Transfer").build();
        }
    }

    @Override
    public Boolean verifySenderAmount(FunderTransferByQrCode request, Double userSenderBalance) {
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

