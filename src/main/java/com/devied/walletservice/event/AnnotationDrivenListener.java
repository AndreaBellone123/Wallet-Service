package com.devied.walletservice.event;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.NoCartsAvailableException;
import com.devied.walletservice.error.PaymentMethodNotAllowedException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.payment.PaymentServiceFactory;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.service.PaymentMethodService;
import com.devied.walletservice.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnnotationDrivenListener {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    UserDataService userDataService;

    @Autowired
    PaymentMethodService paymentMethodService;

    @Autowired
    PaymentServiceFactory paymentServiceFactory;

    @Async
    @EventListener
    public void handleContextStart(CustomSpringEvent customSpringEvent) throws UserNotFoundException, PaymentMethodNotAllowedException, NoCartsAvailableException {

        System.out.println("Success");
        UserData userData = userDataRepository.findByEmail(customSpringEvent.getDonationData().getStreamer()).orElseThrow(UserNotFoundException::new);

        if (userData.getEarned() >= 1000) {
            paymentServiceFactory.create(paymentMethodService.getPayOutMethod(userData.getEmail()).getMethod()).cashOut(userData.getEmail());
            userDataService.cashOut(userData.getEmail());
        }

        System.out.println(Thread.currentThread().getName());
    }
}
