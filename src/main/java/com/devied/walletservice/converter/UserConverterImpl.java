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
        user.setPaypalEmail(current.getPaypalEmail());
        user.setEmail(current.getEmail());
        user.setBought(current.getBought());
        user.setEarned(current.getEarned());
        user.setTotal(user.getBought() + user.getEarned());

        return user;
    }
}
