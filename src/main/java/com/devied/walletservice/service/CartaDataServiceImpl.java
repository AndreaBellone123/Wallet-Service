package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.repository.CartDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartaDataServiceImpl implements CartDataService {

    @Autowired
    CartDataRepository cartDataRepository;

    @Override
    public CartData findCurrent(String email) {
        return cartDataRepository.findTopByEmailOrderByDateDesc(email);
    }
}
