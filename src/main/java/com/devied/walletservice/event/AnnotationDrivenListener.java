package com.devied.walletservice.event;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnnotationDrivenListener {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserDataService userDataService;

    @EventListener
    public void handleContextStart(CustomSpringEvent customSpringEvent) throws UserNotFoundException {

        System.out.println("Success");
        UserData userData = userDataRepository.findByEmail(customSpringEvent.getDonationData().getStreamer()).orElseThrow(UserNotFoundException::new);

        if (userData.getEarned() >= 1000){
            userDataService.DeviedCashOut(userData.getEmail());
        }
    }
}
