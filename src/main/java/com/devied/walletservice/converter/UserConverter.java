package com.devied.walletservice.converter;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;

public interface UserConverter {
    User convert(UserData current);
}
