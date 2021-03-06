package com.devied.walletservice.data;

import com.devied.walletservice.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("users")
public class UserData {

    @Id
    private String id;
    private String email;
    private int earned;
    private int bought;
    private int total;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    public void setBought(int bought) {
        this.bought = bought;
        this.total = this.bought + this.earned;
    }

    public void setEarned(int earned) {
        this.earned = earned;
        this.total = this.bought + this.earned;
    }
}
