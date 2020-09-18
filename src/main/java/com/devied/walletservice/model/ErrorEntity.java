package com.devied.walletservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorEntity {

    private String code;
    private String message;

    public ErrorEntity(String code, String message) {

        this.code = code;
        this.message = message;
    }
}
