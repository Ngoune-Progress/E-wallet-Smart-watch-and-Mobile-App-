package com.example.PayMe.models;

import com.example.PayMe.Entity.BankCard;
import lombok.Data;

import javax.persistence.Column;

public class LinkBankCardRequest {
    private String email;
    private String number;
    private String ccv;
    private String cardType;
    private  String name;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkBankCardRequest() {
    }

    public LinkBankCardRequest(String email, String number, String ccv, String cardType, String name) {
        this.email = email;
        this.number = number;
        this.ccv = ccv;
        this.cardType = cardType;
        this.name = name;
    }
}
