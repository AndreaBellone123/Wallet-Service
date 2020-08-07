package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.service.CartDataService;
import com.devied.walletservice.service.UserDataService;
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

    @Autowired
    UserDataService userDataService;


    @GetMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart getCurrentCart(Authentication auth) {
        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @PatchMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) throws Exception {
        return cartConverter.convert(cartDataService.patchCurrent(auth.getName(), updatedCart.getItemsList()));
    }

    @PostMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public Checkout initialCheckout(Authentication auth) throws Exception {

        CartData cartData = cartDataService.findCurrent(auth.getName());
        return paymentService.initialCheckout(auth.getName(), cartData);
    }

    @PatchMapping("/current/checkout")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public void completeCheckout(@RequestBody Checkout checkout, Authentication auth) throws Exception {

        paymentService.completeCheckout(auth.getName(), checkout);
        userDataService.updateWallet(auth.getName());
        cartDataService.emptyCart(auth.getName(), checkout);

    }
}
