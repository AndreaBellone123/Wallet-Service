package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.*;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.PaypalAccessToken;
import com.devied.walletservice.model.User;
import com.devied.walletservice.service.CartDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaypalService paypalService;
    @Autowired
    CartDataService cartDataService;

    @Override
    public Checkout initialCheckout(String name) throws Exception {
        CartData cartData = cartDataService.findCurrent(name);
        if (cartData.getPaymentMethod().getMethod().equals("paypal")){
            return paypalService.initialCheckout(name, cartData);

        } else if (cartData.getPaymentMethod().getMethod().equals("axerve")){
            return null;//TODO axerveService.initialCheckout(name, cartData);
        }else
            throw new PaymentMethodNotFoundException();
    }

    @Override
    public void completeCheckout(String name, Checkout checkout, CartData cartData) throws Exception {
        if (cartData.getPaymentMethod().getMethod().equals("paypal")){
                paypalService.completeCheckout(name, checkout);

        } else if (cartData.getPaymentMethod().getMethod().equals("axerve")){
            //TODO axerveService.completeCheckout(name, checkout);
        }else
            throw new PaymentMethodNotFoundException();
    }

    @Override
    public User getPaypalUser(PaypalAccessToken paypalAccessToken, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException {

        return paypalService.getUser(paypalAccessToken, email);
    }

    @Override
    public void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException {

        paypalService.cashOut(email);
    }

}