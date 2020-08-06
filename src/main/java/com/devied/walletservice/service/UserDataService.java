package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;

public interface UserDataService  {

    UserData findByEmail(String email);

    void updateWallet(String email) throws Exception;
}
