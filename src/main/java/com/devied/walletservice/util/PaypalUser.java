package com.devied.walletservice.util;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class PaypalUser implements Serializable {

    private String user_id;
    private String [] emails;
}
