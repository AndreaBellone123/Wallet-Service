package com.devied.walletservice.api;

import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Donation;
import com.devied.walletservice.model.User;
import com.devied.walletservice.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
public class Donations {

    @Autowired
    UserDataService userDataService;

    @PatchMapping("/{email:.*}")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public User donate(Authentication auth, @PathVariable(value = "email") String email, @RequestBody Donation donation) throws Exception {
        return userDataService.donate(auth.getName(), email, donation.getAmount());
    }


}
