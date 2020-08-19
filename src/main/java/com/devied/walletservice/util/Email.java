package com.devied.walletservice.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {

    private String value;
    private boolean primary;
    private boolean confirmed;

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                ", primary=" + primary +
                ", confirmed=" + confirmed +
                '}';
    }
}
