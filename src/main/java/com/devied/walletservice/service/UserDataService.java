package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.DuplicatePaymentMethodException;
import com.devied.walletservice.error.PaymentMethodNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.User;


public interface UserDataService {

    UserData findByEmail(String email) throws UserNotFoundException;

    void updateWallet(String email) throws Exception;

    User getWallet(String email) throws Exception;

    User donate(String email, String sid, int amount) throws Exception;

    void DeviedCashOut(String email) throws UserNotFoundException;

    User createWallet(String name);

    User addPaymentMethod(PaymentMethod paymentMethod, String name) throws UserNotFoundException;

    User updateDefaultMethod(String id,PaymentMethod paymentMethod, String name) throws UserNotFoundException, DuplicatePaymentMethodException, PaymentMethodNotFoundException;

    User deletePaymentMethod(String id, String name) throws UserNotFoundException, PaymentMethodNotFoundException;
}
