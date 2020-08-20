package com.devied.walletservice.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {

    @JsonIgnore
    private String user_id;
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
