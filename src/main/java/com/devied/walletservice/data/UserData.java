package com.devied.walletservice.data;

import com.devied.walletservice.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
<<<<<<< HEAD
    private PaymentMethod[] paymentMethod;
=======
    private String paypal_email;
>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf
}
