package com.devied.walletservice.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("donations")
public class DonationData {
    private int amount;
    private String Streamer;
    private String Donor;
}
