package com.devied.walletservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Cart in forbidden status!")
public class CartInForbiddenStatusException extends Exception {

}
