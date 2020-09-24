package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.*;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


public interface UserDataService {

    UserData findByEmail(String email) throws UserNotFoundException;

    void updateWallet(String email) throws Exception;

    User getWallet(String email) throws Exception;

    User donate(String email, String sid, int amount) throws Exception;

    void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException;

    User createWallet(String name) throws SameUserException, UserUnauthorizedException;

    List<PaymentMethod> addPaymentMethod(PaymentMethod paymentMethod, String name) throws UserNotFoundException, PaymentMethodNotFoundException, JsonProcessingException, UserUnauthorizedException;

    List<PaymentMethod> updateDefaultMethod(String id, PaymentMethod paymentMethod, String name) throws UserNotFoundException, DuplicatePaymentMethodException, PaymentMethodNotFoundException, UserUnauthorizedException;

    List<PaymentMethod> deletePaymentMethod(String id, String name) throws UserNotFoundException, PaymentMethodNotFoundException, UserUnauthorizedException;
}
