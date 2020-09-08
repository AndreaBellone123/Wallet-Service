package com.devied.walletservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Checkout {

    private String url;
    private Map<String,Object> parameters = new HashMap();
}
