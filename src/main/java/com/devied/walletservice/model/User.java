package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String email;
    private String paypalEmail;
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
