package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface UserDataService  {

    public UserData findByEmail(String email);
}
