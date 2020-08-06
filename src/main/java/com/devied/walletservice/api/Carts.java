package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.repository.CartDataRepository;
import com.devied.walletservice.service.CartDataService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class Carts {
    private static final String CLIENT_ID = "AeWzs5E743fXTvTCNrRzzaMky1M1DxJ_Pb8xcEj_-hnHnIqiDmuC24YBILsqXdQef-pjp7MFFlhuK31N";
    private static final String CLIENT_SECRET = "EOwAPnAW5oVJmyU0-wphXdgofFQEYXoBPfyVaCoweb-DlyVp2Yp7rLShjYIcYDYzH3OiFsPV0WTdbxK6";
    private static final String MODE = "sandbox";

    @Autowired
    CartDataService cartDataService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    CartConverter cartConverter;

    @Autowired
    CartDataRepository cartDataRepository;

    @GetMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart getCurrentCart(Authentication auth) {
        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @PatchMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) {
        return cartConverter.convert(cartDataService.patchCurrent(auth.getName(), updatedCart.getItemsList()));
    }

    @PostMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Checkout initialCheckout(Authentication auth) {

        CartData cartData = cartDataService.findCurrent(auth.getName());

        return paymentService.initialCheckout(auth.getName(), cartData);
    }
    
    @PatchMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Payment completeCheckout(@RequestBody Checkout checkout, Authentication auth) throws PayPalRESTException {

        return paymentService.completeCheckout(auth.getName(), checkout);
    }

}
