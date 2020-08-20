package com.devied.walletservice.api;

import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.PaypalUser;
import com.devied.walletservice.model.User;
import com.devied.walletservice.service.UserDataService;
import com.devied.walletservice.util.PaypalServiceImpl;
import com.devied.walletservice.util.PaypalUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/wallets")
public class Wallets {

    @Autowired
    UserDataService userDataService;

    @Autowired
    PaypalServiceImpl restServiceImpl;

    @GetMapping(produces = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User getWallet(Authentication auth) throws Exception {
        return userDataService.getWallet(auth.getName());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
<<<<<<< HEAD
    public User createWallet(Authentication auth) throws IOException {
=======
    public User createWallet(Authentication auth) {
>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf
        return userDataService.createWallet(auth.getName());
    }

   /* @PutMapping(path = "/{pid}")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<User> buyProduct(@PathVariable(value = "pid")String pid,Authentication auth) throws Exception{ return userDataService.buyProduct(auth.getName(),pid); } */

    @GetMapping(path = "/getEmail")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public PaypalUser getEmail() throws JsonProcessingException {
        return restServiceImpl.getEmail();
    }
}
