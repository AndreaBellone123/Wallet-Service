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

public interface PaymentService {

    Checkout initialCheckout(String name) throws Exception;

    User getUser(PaypalAccessToken paypalAccessToken, String email) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException;

    void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException;

    void completeCheckout(CartData cartData, Checkout checkout) throws Exception;
}
