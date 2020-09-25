package com.devied.walletservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private int bonusTokens;
    private int amount;
    private double price;
}
