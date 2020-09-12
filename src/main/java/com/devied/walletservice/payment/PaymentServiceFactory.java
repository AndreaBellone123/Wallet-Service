package com.devied.walletservice.payment;

import com.devied.walletservice.error.PaymentMethodNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceFactory {

    @Autowired
    PaypalService paypalService;

    public PaymentService create(String method) throws PaymentMethodNotAllowedException {
        if("paypal".equals(method)){
            return paypalService;
        }else{
            throw new PaymentMethodNotAllowedException();
        }
    }
}
