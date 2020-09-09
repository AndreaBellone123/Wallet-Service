package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {

    private String email;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    private int earned;
    private int bought;
    private int total;

    public void setBought(int bought) {
        this.bought = bought;
        this.total = this.bought + this.earned;
    }

    public void setEarned(int earned) {
        this.earned = earned;
        this.total = this.bought + this.earned;
    }
}
