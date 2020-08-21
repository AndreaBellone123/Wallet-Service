package com.devied.walletservice.payment;

import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    User getUser(String token, String email) throws JsonProcessingException, UserNotFoundException;

    void cashOut(String email) throws UserNotFoundException;
}
