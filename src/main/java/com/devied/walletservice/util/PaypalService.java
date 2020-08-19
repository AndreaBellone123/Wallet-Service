package com.devied.walletservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaypalService {

    PaypalUser getEmail() throws JsonProcessingException;
}
