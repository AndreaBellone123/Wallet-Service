package com.devied.walletservice.api;

import com.devied.walletservice.converter.CartConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Cart;
import com.devied.walletservice.model.Item;
import com.devied.walletservice.repository.CartDataRepository;
import com.devied.walletservice.service.CartDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Secured(IdentityRole.AUTHORITY_USER)
    public Cart getCurrentCart(Authentication auth) {

        return cartConverter.convert(cartDataService.findCurrent(auth.getName()));
    }

    @PatchMapping("/current")
    @Secured(IdentityRole.AUTHORITY_USER)
    public Cart updateCart(Authentication auth, @RequestBody Cart updatedCart) {
        CartData cartData = cartDataService.findCurrent(auth.getName());
        List<Item> itemsList = cartData.getItemsList();
        HashMap<String, Item> itemsMap = new HashMap<>();
        for (Item i : itemsList) {

            itemsMap.put(i.getId(), i);
        }
        for (Item i : updatedCart.getItemsList()) {
            itemsMap.put(i.getId(), i);
        }
        cartData.setItemsList(new ArrayList<>(itemsMap.values()));
        cartDataRepository.save(cartData);

        return cartConverter.convert(cartData);

    }

}
