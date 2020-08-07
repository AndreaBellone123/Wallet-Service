package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Cart {

    @Id
    private String id;
    private double subTotal;
    private List<Item> itemsList;
}
