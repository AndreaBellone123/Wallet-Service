package com.devied.walletservice.model;

import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.List;

public class Cart {

    @Id
    private String id;
    private double subTotal;
    private List<Item> itemsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }
}
