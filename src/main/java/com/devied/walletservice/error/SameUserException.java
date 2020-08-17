package com.devied.walletservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Cannot donate tokens to yourself!!")
public class SameUserException extends Exception {
}
