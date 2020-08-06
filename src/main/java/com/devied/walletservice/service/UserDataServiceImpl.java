package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.repository.CartDataRepository;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    CartDataRepository cartDataRepository;

    @Autowired
    CartDataService cartDataService;

    @Autowired
    ProductDataRepository productDataRepository;

    @Override
    public UserData findByEmail(String email) {
       return userDataRepository.findByEmail(email);
    }

    @Override
    public void updateWallet(String email) throws Exception {

       UserData userData = userDataRepository.findByEmail(email);
       CartData cartData = cartDataService.findCurrent(email);
       ProductData productdata = productDataRepository.findById(cartData.getItemsList().get(0).getId()).orElseThrow(() -> new Exception("No Products Found"));
       userData.setBoughtTokens(userData.getBoughtTokens() + productdata.getAmount());
       userDataRepository.save(userData);
    }
}
