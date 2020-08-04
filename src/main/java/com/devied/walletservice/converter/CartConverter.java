package com.devied.walletservice.converter;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.Cart;

public interface CartConverter {

    Cart convert(CartData current);
}
