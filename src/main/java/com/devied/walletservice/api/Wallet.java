package com.devied.walletservice.api;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class Wallet {
    @Autowired
    UserDataRepository userDataRepository;
    ProductDataRepository productDataRepository;

    @GetMapping(path ="/wallet", produces = "application/Json")
    public User getWallet(){
        String id = "123";
        UserData userData = userDataRepository.findById(id).orElse(null);
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setBoughtToken(userData.getBoughtToken());
        user.setEarnedToken(userData.getEarnedToken());
        return null;
    }
}
