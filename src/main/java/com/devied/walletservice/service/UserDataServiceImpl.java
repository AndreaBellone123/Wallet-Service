package com.devied.walletservice.service;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;
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
        userData.setBought(userData.getBought() + productdata.getAmount());
        userDataRepository.save(userData);
    }

    /*@Override
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
    }*/

    @Override
    public User getWallet(String email) {
       
        UserData userData = userDataRepository.findByEmail(email);
        if (userData == null){
            UserData userData1 = new UserData();
            userData1.setEmail(email);
            userDataRepository.save(userData1);
            User user = userConverter.convert(userData1);
            return user;
        }else {
            User user = userConverter.convert(userData);
            return user;
        }
    }

    @Override
    public User donate(String name,String sid,int amount) throws Exception {

        UserData donatingUser = userDataRepository.findByEmail(name);

        UserData streamingUser = userDataRepository.findByEmail(sid);

        if(donatingUser.getBought() >= amount && userDataRepository.findByEmail(sid) != null && amount > 0){

            streamingUser.setEarned(streamingUser.getEarned() + amount );

            donatingUser.setBought(donatingUser.getBought() - amount);

            userDataRepository.save(streamingUser);

            userDataRepository.save(donatingUser);

            return userConverter.convert(donatingUser);

        }

        else {
            throw new Exception("There was an error processing your request!");
        }

    }
}
