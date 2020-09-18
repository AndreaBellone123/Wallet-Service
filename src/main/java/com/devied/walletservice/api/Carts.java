package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.error.NoCartsAvailableException;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.payment.PaymentServiceFactory;
import com.devied.walletservice.service.CartDataService;
import com.devied.walletservice.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class Carts {

    @Autowired
    CartDataService cartDataService;

    @Autowired
    PaymentServiceFactory paymentServiceFactory;

    @Autowired
    CartConverter cartConverter;

    @Autowired
    PaymentMethodService paymentMethodService;

    @CrossOrigin(origins = "http://localhost:8000")
    @GetMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart getCurrentCart(Authentication auth) throws NoCartsAvailableException {

        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @PatchMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) throws Exception {

        return cartConverter.convert(cartDataService.patchCurrent(auth.getName(), updatedCart.getItemsList(), updatedCart.getPaymentMethod()));
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @PostMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Checkout initialCheckout(Authentication auth) throws Exception {

        return paymentServiceFactory.create(paymentMethodService.getPayInMethod(auth.getName()).getMethod()).initialCheckout(auth.getName());
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @PatchMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public void completeCheckout(@RequestBody Checkout checkout, Authentication auth) throws Exception {

        paymentServiceFactory.create(paymentMethodService.getPayInMethod(auth.getName()).getMethod()).completeCheckout(cartDataService.findCurrent(auth.getName()), checkout);
        cartDataService.checkoutCurrent(checkout, auth.getName());

    }
}
