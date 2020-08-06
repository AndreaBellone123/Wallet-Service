package com.devied.walletservice.data;

import com.devied.walletservice.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document("Carts")
public class CartData {

    @Id
    private String id;
    private float subtotal;
    private float tax;
    private float total;
    private List<Item> itemsList;
    private Date date = new Date();
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal);
    }

    public String getTax() {
        return String.format("%.2f", tax);
    }

    public String getTotal() {
        return String.format("%.2f", total);
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public void setTotal(float total) {
        this.total = total;
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
