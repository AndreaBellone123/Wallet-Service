package com.devied.walletservice.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PaypalUser implements Serializable {

    private String user_id;
    private List<Email> emails;

}
