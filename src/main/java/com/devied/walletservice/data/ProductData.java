package com.devied.walletservice.data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document("products")
public class ProductData {

    private String id;
    private String name;
    private double discount;
    private int amount;
    private double price;
}
