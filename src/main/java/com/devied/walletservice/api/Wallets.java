package com.devied.walletservice.api;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class Wallets {
    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    UserConverter userConverter;

    @GetMapping(produces = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<User> getWallet(Authentication auth)  {

        UserData userData = userDataRepository.findByEmail(auth.getName());
        User user = userConverter.convert(userData);
        var headers = new HttpHeaders();
        headers.add("Funds currently available on your account", "Wallets Controller");
        return ResponseEntity.accepted().headers(headers).body(user);
    }

    @PutMapping(path = "/{pid}")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<User> buyProduct(@PathVariable(value = "pid")String pid,Authentication auth) throws Exception{

        UserData userData = userDataRepository.findByEmail(auth.getName());
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));

        if(userData.getAvailableFunds() >= productData1.getPrice()) {
            userData.setBoughtTokens(userData.getBoughtTokens() + productData1.getTokens());
            userData.setAvailableFunds(userData.getAvailableFunds() - productData1.getPrice());
            userDataRepository.save(userData);
            User user = userConverter.convert(userData);
            var headers = new HttpHeaders();
            headers.add("Tokens bought successfully", "Wallets Controller");
            return ResponseEntity.accepted().headers(headers).body(user);
        }
        else {
            var headers = new HttpHeaders();
            headers.add("Insufficient Funds!", "Wallets Controller");
            User user = new User();
            user.setBoughtTokens(userData.getBoughtTokens());
            user.setEarnedTokens(userData.getEarnedTokens());
            user.setAvailableFunds(userData.getAvailableFunds());
            user.setEmail(auth.getName());
            return ResponseEntity.badRequest().headers(headers).body(user);
        }
    }
}
