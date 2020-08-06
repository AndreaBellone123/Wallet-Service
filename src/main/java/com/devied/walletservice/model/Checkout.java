package com.devied.walletservice.model;

public class Checkout {

    private String paymentId;
    private String token;
    private String payerId;
    private String url;

    public Checkout(String paymentId,String token,String payerId,String url) {

        this.payerId = payerId;
        this.token = token;
        this.paymentId = paymentId;
        this.url = url;
    }

    public Checkout(){
        
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
