package com.devied.walletservice.event;

import com.devied.walletservice.data.DonationData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CustomSpringEvent extends ApplicationEvent {

    private DonationData donationData;

    public CustomSpringEvent(Object source, DonationData donationData) {
        super(source);
        this.donationData = donationData;
    }
}