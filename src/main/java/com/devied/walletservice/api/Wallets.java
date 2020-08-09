package com.devied.walletservice.api;

import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.User;
import com.devied.walletservice.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class Wallets {

    // TODO implement channel subscriptions,unlocking of exclusive content

    @Autowired
    UserDataService userDataService;

    @GetMapping(produces = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User getWallet(Authentication auth) {
        return userDataService.getWallet(auth.getName());
    }

   /* @PutMapping(path = "/{pid}")
    @Secured({IdentityRole.AUTHORITY_USER,IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<User> buyProduct(@PathVariable(value = "pid")String pid,Authentication auth) throws Exception{ return userDataService.buyProduct(auth.getName(),pid); } */

    @PatchMapping("/{sid}")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User donate(Authentication auth, @PathVariable(value = "sid") String sid, @RequestBody int amount) throws Exception {

        return userDataService.donate(auth.getName(), sid, amount);

    }
}
