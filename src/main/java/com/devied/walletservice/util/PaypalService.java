package com.devied.walletservice.util;

import com.devied.walletservice.error.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    PaypalUser getUser(String token,String email) throws JsonProcessingException, UserNotFoundException;
}
