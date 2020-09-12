package com.devied.walletservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "method")
@JsonSubTypes({@JsonSubTypes.Type(value = PaypalMethod.class, name = "paypal"), @JsonSubTypes.Type(value = AxerveMethod.class, name = "axerve")})
public class PaymentMethod {

    private String uuid;
    private String method;
    private boolean payInMethod;
    private boolean payOutMethod;

    public PaymentMethod(String method) {

        this.method = method;
        this.uuid = UUID.randomUUID().toString().replace("-","");
    }
}
