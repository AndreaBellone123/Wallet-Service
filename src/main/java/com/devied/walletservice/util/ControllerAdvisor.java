package com.devied.walletservice.util;

import com.devied.walletservice.error.BaseError;
import com.devied.walletservice.model.ErrorEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseError.class})
    protected ResponseEntity<Object> handleConflict(BaseError ex, WebRequest request) {
        ErrorEntity errorEntity = new ErrorEntity(ex.getError().getCode(), ex.getError().getMessage());
        return handleExceptionInternal(ex, errorEntity ,new HttpHeaders(),  HttpStatus.CONFLICT, request);
    }
}