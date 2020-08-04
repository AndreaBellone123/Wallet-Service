package com.devied.walletservice.data;

import com.devied.walletservice.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("carts")
public class CartData {

    @Id
    private String id;
    private double subTotal;
    private List<Item> itemsList;
    private Date date;
    private String email;

    public CartData(String id, double subTotal, List<Item> itemsList) {
        this.id = id;
        this.subTotal = subTotal;
        this.itemsList = itemsList;
        this.date = new Date();
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
