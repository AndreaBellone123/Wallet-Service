package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private double discount;
    private int amount;
    private double price;
}
