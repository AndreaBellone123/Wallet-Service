package com.devied.walletservice.data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("Products")
public class ProductData {
    private String id;
    private String name;
    private double discount;
    private int tokens;
    private double price;

    public ProductData(String name,int tokens, double price ,double discount) {

        this.name = name;
        this.tokens = tokens;
        this.discount = discount;
        this.price = price;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
