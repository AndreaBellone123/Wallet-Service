package com.devied.walletservice.util;

import com.devied.walletservice.error.BaseError;
import com.devied.walletservice.model.ErrorEntity;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseError.class})
    protected ResponseEntity<Object> handleConflict(BaseError ex, WebRequest request) {
<<<<<<< HEAD
        ErrorEntity errorEntity = new ErrorEntity(ex.getError().name(), ex.getError().getMessage());
        log.error(ex.getError().getMessage(), ex);
        return handleExceptionInternal(ex, errorEntity , HttpHeaders.EMPTY,  HttpStatus.valueOf(ex.getError().getCode()), request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        ErrorEntity errorEntity = new ErrorEntity(Error.err_000.name(), Error.err_000.getMessage());
        log.error(Error.err_000.getMessage(), ex);
        return handleExceptionInternal(ex, errorEntity , HttpHeaders.EMPTY,  HttpStatus.valueOf(Error.err_000.getCode()), request);
=======
        ErrorEntity errorEntity = new ErrorEntity(ex.getError().getCode(), ex.getError().getMessage());
        return handleExceptionInternal(ex, errorEntity ,new HttpHeaders(),  HttpStatus.CONFLICT, request);
    }

/*
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        ErrorEntity errorEntity = new ErrorEntity(Error.err_000.getCode(),Error.err_000.getMessage());
        return handleExceptionInternal(ex, errorEntity ,new HttpHeaders(),  HttpStatus.CONFLICT, request);
>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf
    }
*/
}