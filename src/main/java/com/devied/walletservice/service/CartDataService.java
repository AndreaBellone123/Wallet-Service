package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.NoCartsAvailableException;
import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.model.Checkout;
import java.util.List;

public interface CartDataService {

    CartData findCurrent(String email) throws NoCartsAvailableException;

    CartData patchCurrent(String email, List<CartItem> cartItems, String paymentMethod) throws Exception;

    void emptyCart(String email, Checkout checkout) throws Exception;

    void updateState(CartData cartData) throws Exception;

    void finalState(String name) throws Exception;

    void checkoutCurrent(Checkout checkout, String name) throws Exception;
}
