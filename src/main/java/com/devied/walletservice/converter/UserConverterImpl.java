package com.devied.walletservice.converter;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserConverterImpl implements UserConverter {
    @Override
    public User convert(UserData current) {
        User user = new User();
        user.setEmail(current.getEmail());
        user.setAdmin(current.isAdmin());
        user.setAvailableFunds(current.getAvailableFunds());
        user.setBoughtTokens(current.getBoughtTokens());
        user.setEarnedTokens(current.getEarnedTokens());
        return user;
    }
}
