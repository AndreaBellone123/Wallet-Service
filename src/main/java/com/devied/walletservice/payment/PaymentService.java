package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.DuplicatePaymentMethodException;
import com.devied.walletservice.error.PaypalUserNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {

    Checkout initialCheckout(String name, CartData cartData) throws Exception;

    User getPaypalUser(String token, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException;

    void PaypalCashOut(String email) throws UserNotFoundException;

    void completeCheckout(String name, Checkout checkout, CartData cartData) throws Exception;
}
