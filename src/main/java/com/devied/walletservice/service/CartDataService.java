package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.model.Checkout;

import java.util.List;

public interface CartDataService {

    CartData findCurrent(String email);

    CartData patchCurrent(String email, List<CartItem> cartItems) throws Exception;

    void emptyCart(String email, Checkout checkout) throws Exception;

    void updateState(CartData cartData) throws Exception;

    void finalState(String name) throws Exception;
}
