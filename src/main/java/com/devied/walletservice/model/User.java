package com.devied.walletservice.model;

public class User {
    private String email;
    private int earnedToken;
    private int boughtToken;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEarnedToken() {
        return earnedToken;
    }

    public void setEarnedToken(int earnedToken) {
        this.earnedToken = earnedToken;
    }

    public int getBoughtToken() {
        return boughtToken;
    }

    public void setBoughtToken(int boughtToken) {
        this.boughtToken = boughtToken;
    }
}
