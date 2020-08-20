package com.devied.walletservice.service;

import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    User getUser(String token, String email) throws JsonProcessingException, UserNotFoundException;
}
