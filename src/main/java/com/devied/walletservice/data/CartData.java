package com.devied.walletservice.data;

import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.util.CartStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Document("carts")
public class CartData {

    @Id
    private String id;
    private double subtotal;
    private double tax;
    private double total;
    private List<CartItem> itemsList = new ArrayList<>();
    private Date date = new Date();
    private String email;
    private String currency = "EUR";
    private CartStatus status = CartStatus.Prepared;

    public String setSubtotal() {
        return String.format(Locale.US, "%.2f", subtotal);
    }

    public void setSubtotal(double subtotal) {

        this.subtotal = subtotal;
        this.total = this.subtotal + this.tax;
    }

    public String setTax() {
        return String.format(Locale.US, "%.2f", tax);
    }

    public void setTax(double tax) {

        this.tax = tax;
        this.total = this.subtotal + this.tax;
    }

    public String formatTotal() {
        return String.format(Locale.US, "%.2f", total);
    }

    public void setTotal(double total) throws Exception {

        throw new Exception("Not Supported");
    }
}
