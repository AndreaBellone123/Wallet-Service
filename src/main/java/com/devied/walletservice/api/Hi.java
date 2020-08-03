package com.devied.walletservice.api;

import com.devied.walletservice.identity.IdentityRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class Hi {

    @GetMapping
    @Secured(IdentityRole.AUTHORITY_USER)
    public String hi(Authentication auth) {
        return "Hi " + auth.getName();
    }

}
