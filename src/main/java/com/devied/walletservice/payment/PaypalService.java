package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.DuplicatePaymentMethodException;
import com.devied.walletservice.error.PaymentMethodNotAllowedException;
import com.devied.walletservice.error.PaypalUserNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.PaypalAccessToken;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    Checkout initialCheckout(String name, CartData cartData) throws Exception;

    void completeCheckout(String name, Checkout checkout) throws Exception;

    User getUser(PaypalAccessToken paypalAccessToken, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException;

    void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException;
}
