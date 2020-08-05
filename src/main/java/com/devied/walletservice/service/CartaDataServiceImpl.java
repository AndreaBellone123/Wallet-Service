package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.Item;
import com.devied.walletservice.repository.CartDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CartaDataServiceImpl implements CartDataService {

    @Autowired
    CartDataRepository cartDataRepository;

    @Override
    public CartData findCurrent(String email) {
        return cartDataRepository.findTopByEmailOrderByDateDesc(email);
    }

    @Override
    public CartData patchCurrent(String email, List<Item> items) {

        CartData cartData = findCurrent(email); // Fixed null pointer exception
        if (cartData == null) {
            cartData = new CartData();
            cartData.setEmail(email);
        }

        // TODO add ID validation

        Set<Item> itemsSet = new HashSet<>(cartData.getItemsList()); // Non permette duplicati(controllo HashCode)
        itemsSet.removeAll(items);
        itemsSet.addAll(items);

        cartData.setItemsList(new ArrayList<>(itemsSet));

        cartDataRepository.save(cartData);
        return cartData;
    }
}
