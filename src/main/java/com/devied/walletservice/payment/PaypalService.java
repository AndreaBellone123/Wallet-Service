package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.PaypalUserNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    Checkout initialCheckout(String name, CartData cartData) throws Exception;

    void completeCheckout(String name, Checkout checkout) throws Exception;

    User getUser(String token, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException;

    void cashOut(String email) throws UserNotFoundException;
}
