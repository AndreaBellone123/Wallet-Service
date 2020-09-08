package com.devied.walletservice.api;

import com.devied.walletservice.error.PaymentMethodNotFoundException;
import com.devied.walletservice.error.PaypalUserNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.User;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.service.UserDataService;
import com.devied.walletservice.model.PaymentMethod;
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

    @PatchMapping
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User linkToPaypal(@RequestBody String token,Authentication auth) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException {
        return paymentService.getPaypalUser(token, auth.getName());
    }

    @PostMapping
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User createWallet(Authentication auth) throws JsonProcessingException, UserNotFoundException {
        return userDataService.createWallet(auth.getName());
    }

    @PatchMapping(path = "/methods")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User addPaymentMethod(@RequestBody PaymentMethod paymentMethod, Authentication auth) throws UserNotFoundException {
        return userDataService.addPaymentMethod(paymentMethod, auth.getName());
    }

}
