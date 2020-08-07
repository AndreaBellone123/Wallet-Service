package com.devied.walletservice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Document("transactions")
public class TransactionData {
 //TODO PaymentId--
    @Id
    private String id;
    private String paymentId;
    private String email;
    private ProductData productData;
    private Date date = new Date();
    private String url;
}
