package com.devied.walletservice.data;

import com.devied.walletservice.model.Payout;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Getter
@Setter
@Document("transactions")
public class TransactionData {

    @Id
    private String id;
    private String paymentId;
    private String email;
    private ProductData productData;
    private Date date = new Date();
    private String url;
    private Payout payout;

}
