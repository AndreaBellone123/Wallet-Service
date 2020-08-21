package com.devied.walletservice.api;

import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.User;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.service.UserDataService;
import com.devied.walletservice.payment.PaypalServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class Wallets {

    @Autowired
    UserDataService userDataService;

    @Autowired
    PaymentService paymentService;

    @GetMapping(produces = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User getWallet(Authentication auth) throws Exception {
        return userDataService.getWallet(auth.getName());
    }

    /*@PostMapping(produces = "application/json", consumes = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User createWallet(Authentication auth) throws IOException {

        return userDataService.createWallet(auth.getName());

    }*/

   /* @PutMapping(path = "/{pid}")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<User> buyProduct(@PathVariable(value = "pid")String pid,Authentication auth) throws Exception{ return userDataService.buyProduct(auth.getName(),pid); } */

    @PostMapping//(produces = "application/json", consumes = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User createWallet(@RequestBody String token,Authentication auth) throws JsonProcessingException, UserNotFoundException {
        return paymentService.getPaypalUser(token, auth.getName());
    }
}
