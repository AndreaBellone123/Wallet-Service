package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;

public interface CartDataService {

    CartData findCurrent(String email);

}
