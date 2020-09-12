package com.devied.walletservice.converter;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartConverterImpl implements CartConverter {

    @Override
    public Cart convert(CartData current) {

        Cart cart = new Cart();
        cart.setSubTotal(current.getSubtotal());
        cart.setItemsList(current.getItemsList());
        cart.setId(current.getId());
        cart.setPaymentMethod(current.getPaymentMethod());
        return cart;
    }
}
