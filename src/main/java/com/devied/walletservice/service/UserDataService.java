package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;
import org.springframework.http.ResponseEntity;

public interface UserDataService  {

    UserData findByEmail(String email);

    void updateWallet(String email) throws Exception;

    ResponseEntity<User> buyProduct(String email, String pid) throws Exception;

    ResponseEntity<User> getWallet(String email);
}
