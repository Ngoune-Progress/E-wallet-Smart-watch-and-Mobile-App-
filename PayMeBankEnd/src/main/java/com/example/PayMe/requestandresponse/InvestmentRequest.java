package com.example.PayMe.requestandresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentRequest {
    private String sender;
    private String receiver;

    private Long postId;
    private Double amount;
}
