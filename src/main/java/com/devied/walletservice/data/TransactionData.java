package com.devied.walletservice.data;

import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.Payout;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document("transactions")
public class TransactionData {

    @Id
    private String id;
    private String paymentId;
    private String email;
    private List<ProductData> productDataList = new ArrayList<>();
    private Date date = new Date();
    private String url;
    private Payout payout;
    private PaymentMethod paymentMethod;
}
