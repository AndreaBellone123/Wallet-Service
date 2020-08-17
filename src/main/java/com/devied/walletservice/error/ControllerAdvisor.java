package com.devied.walletservice.error;

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

    @ExceptionHandler(value = BaseError.class)
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setMessage(-------);
        errorEntity.setCode(-------);
        return handleExceptionInternal(ex, errorEntity,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
