package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.error.NoCartsAvailableException;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.service.CartDataService;
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
    PaymentService paymentService;

    @Autowired
    CartConverter cartConverter;

    @GetMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart getCurrentCart(Authentication auth) throws NoCartsAvailableException {
        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @PatchMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) throws Exception {
        return cartConverter.convert(cartDataService.patchCurrent(auth.getName(), updatedCart.getItemsList(), updatedCart.getPaymentMethod()));
    }

    @PostMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Checkout initialCheckout(Authentication auth) throws Exception {

        return paymentService.initialCheckout(auth.getName());
    }

    @PatchMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public void completeCheckout(@RequestBody Checkout checkout, Authentication auth) throws Exception {

        cartDataService.checkoutCurrent(checkout, auth.getName());

    }
}
