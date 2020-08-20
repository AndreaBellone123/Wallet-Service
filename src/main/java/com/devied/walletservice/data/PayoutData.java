package com.devied.walletservice.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("payouts")
public class PayoutData {
/*TODO implementare entity con attributi e repository e service eventuali
    {
        "sender_batch_header": {
            "sender_batch_id": "2014021801",
            "recipient_type": "EMAIL",
            "email_subject": "You have money!",
            "email_message": "You received a payment. Thanks for using our service!"
    },
        "items": [
            {
            "amount": {
                "value": "10.00",
                "currency": "EUR"
            },
            "sender_item_id": "201403140001",
            "recipient_wallet": "PAYPAL",
            "receiver": " receiver@example.com"
            }
        ]
    }*/
}
