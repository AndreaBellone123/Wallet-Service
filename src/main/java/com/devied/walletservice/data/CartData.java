package com.devied.walletservice.data;

import com.devied.walletservice.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
}
