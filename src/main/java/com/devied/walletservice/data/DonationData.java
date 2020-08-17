package com.devied.walletservice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("donations")
@Getter
@Setter
@NoArgsConstructor
public class DonationData {

    private int amount;
    private String streamer;
    private String donor;

}
