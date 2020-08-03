package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;

public interface UserDataService  {

    public UserData findByEmail(String email);
}
