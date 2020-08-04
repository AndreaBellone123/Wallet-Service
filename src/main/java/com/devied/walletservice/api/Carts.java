package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.repository.CartDataRepository;
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
    CartConverter cartConverter;

    @Autowired
    CartDataRepository cartDataRepository;

    @GetMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public Cart getCurrentCart(Authentication auth) {
        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @PatchMapping("/current")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) {
        CartData cartData = cartDataService.findCurrent(auth.getName());
        cartData.setItemsList(updatedCart.getItemsList());
        cartDataRepository.save(cartData);
        return cartConverter.convert(cartData);
    }
}
