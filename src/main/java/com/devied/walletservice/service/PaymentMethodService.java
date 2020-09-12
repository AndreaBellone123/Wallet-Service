package com.devied.walletservice.service;

import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.PaymentMethod;

public interface PaymentMethodService {

    public PaymentMethod getPayInMethod(String email) throws UserNotFoundException;

    public PaymentMethod getPayOutMethod(String email) throws UserNotFoundException;
}
