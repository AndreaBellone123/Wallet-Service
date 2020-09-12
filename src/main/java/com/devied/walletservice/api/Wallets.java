package com.devied.walletservice.api;

import com.devied.walletservice.error.*;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.PaypalAccessToken;
import com.devied.walletservice.model.User;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.service.UserDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public User linkToPaypal(@RequestBody PaypalAccessToken paypalAccessToken, Authentication auth) throws JsonProcessingException, UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException {
        return paymentService.getPaypalUser(paypalAccessToken, auth.getName());
    }

    @PostMapping
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User createWallet(Authentication auth) throws SameUserException {
        return userDataService.createWallet(auth.getName());
    }

    @PostMapping(path = "/methods")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public List<PaymentMethod> addPaymentMethod(@RequestBody PaymentMethod paymentMethod, Authentication auth) throws UserNotFoundException, PaymentMethodNotFoundException, JsonProcessingException {
        return userDataService.addPaymentMethod(paymentMethod, auth.getName());
    }

    @PatchMapping(path = "/methods/{id}")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public List<PaymentMethod> updatePaymentMethod(@PathVariable(value = "id") String id, @RequestBody PaymentMethod paymentMethod, Authentication auth) throws UserNotFoundException, PaymentMethodNotFoundException, DuplicatePaymentMethodException {
        return userDataService.updateDefaultMethod(id, paymentMethod, auth.getName());
    }

    @DeleteMapping(path = "/methods/{id}")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public List<PaymentMethod> deletePaymentMethod(@PathVariable(value = "id") String id, Authentication authentication) throws UserNotFoundException, PaymentMethodNotFoundException {

        return userDataService.deletePaymentMethod(id, authentication.getName());
    }


}
