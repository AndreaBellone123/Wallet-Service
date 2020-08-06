package com.devied.walletservice.data;

import com.devied.walletservice.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Document("carts")
public class CartData {

    @Id
    private String id;
    private double subtotal;
    private double tax;
    private double total;
    private List<Item> itemsList;
    private Date date = new Date();
    private String email;

    public String getSubtotal() {
        return String.format(Locale.US,"%.2f", subtotal);
    }

    public String getTax() {
        return String.format(Locale.US,"%.2f", tax);
    }

    public String getTotal() {
        return String.format(Locale.US, "%.2f", total);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setTotal(double total) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
