package com.devied.walletservice.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;

@Getter
@Setter
@Document("products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductData {

    private String id;
    private String name;
    private int bonusTokens;
    private int amount;
    private double price;
    private int quantity;

    public String setPrice() {
        return String.format(Locale.US, "%.2f", price);
    }

}
