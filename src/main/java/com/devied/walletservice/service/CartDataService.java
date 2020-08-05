package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.Item;

import java.util.List;

public interface CartDataService {

    CartData findCurrent(String email);

    CartData patchCurrent(String email, List<Item> items);

}
