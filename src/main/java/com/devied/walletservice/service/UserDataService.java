package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.User;


public interface UserDataService {

    UserData findByEmail(String email) throws UserNotFoundException;

    void updateWallet(String email) throws Exception;

    // ResponseEntity<User> buyProduct(String email, String pid) throws Exception;

    User getWallet(String email) throws Exception;

    User donate(String email, String sid, int amount) throws Exception;

    void cashOut(String email) throws UserNotFoundException;
}
