package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    UserDataRepository userDataRepository;

    @Override
    public UserData findByEmail(String email) {
       return userDataRepository.findByEmail(email);
    }
}
