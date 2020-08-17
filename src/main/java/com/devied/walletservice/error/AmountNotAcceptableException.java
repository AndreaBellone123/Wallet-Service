package com.devied.walletservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE,reason = "Invalid amount!")
public class AmountNotAcceptableException extends Exception {
}
