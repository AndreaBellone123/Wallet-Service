package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.DuplicatePaymentMethodException;
import com.devied.walletservice.error.PaymentMethodNotFoundException;
import com.devied.walletservice.error.PaypalUserNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaypalService paypalService;

    @Override
    public Checkout initialCheckout(String name, CartData cartData) throws Exception {

        if (cartData.getPaymentMethod().equals("paypal")){
            return paypalService.initialCheckout(name, cartData);

        } else if (cartData.getPaymentMethod().equals("axerve")){
            return null;//TODO axerveService.initialCheckout(name, cartData);
        }else
            throw new PaymentMethodNotFoundException();
    }

    @Override
    public void completeCheckout(String name, Checkout checkout, CartData cartData) throws Exception {
        if (cartData.getPaymentMethod().equals("paypal")){
                paypalService.completeCheckout(name, checkout);

        } else if (cartData.getPaymentMethod().equals("axerve")){
            //TODO axerveService.completeCheckout(name, checkout);
        }else
            throw new PaymentMethodNotFoundException();
    }

    @Override
    public User getPaypalUser(String token, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException {

        return paypalService.getUser(token, email);
    }

    @Override
    public void PaypalCashOut(String email) throws UserNotFoundException {

        paypalService.cashOut(email);
    }

}