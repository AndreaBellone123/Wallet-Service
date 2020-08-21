package com.devied.walletservice.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payout {

    private String senderBatchId;
    private String receiver;
    private String amount;
}
