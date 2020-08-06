package com.devied.walletservice.service;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.CartDataRepository;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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


    @Autowired
    UserConverter userConverter;

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

    @Override
    public ResponseEntity<User> buyProduct(String email, String pid) throws Exception {

        UserData userData = userDataRepository.findByEmail(email);
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));

        if(userData.getAvailableFunds() >= productData1.getPrice()) {
            userData.setBoughtTokens(userData.getBoughtTokens() + productData1.getAmount());
            userData.setAvailableFunds(userData.getAvailableFunds() - productData1.getPrice());
            userDataRepository.save(userData);
            User user = userConverter.convert(userData);
            var headers = new HttpHeaders();
            headers.add("Tokens bought successfully", "Wallets Controller");
            return ResponseEntity.accepted().headers(headers).body(user);
        }
        else {
            var headers = new HttpHeaders();
            headers.add("Insufficient Funds!", "Wallets Controller");
            User user = userConverter.convert(userData);
            return ResponseEntity.badRequest().headers(headers).body(user);
        }
    }

    @Override
    public ResponseEntity<User> getWallet(String email) {

        UserData userData = userDataRepository.findByEmail(email);
        User user = userConverter.convert(userData);
        var headers = new HttpHeaders();
        headers.add("Funds currently available on your account", "Wallets Controller");
        return ResponseEntity.accepted().headers(headers).body(user);
    }
}
