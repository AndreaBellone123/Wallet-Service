package com.devied.walletservice.model;

public class User {

    private String email;
    private int earnedTokens;
    private int boughtTokens;
    private boolean isAdmin;
    private double availableFunds;


    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEarnedTokens() {
        return earnedTokens;
    }

    public void setEarnedTokens(int earnedTokens) {
        this.earnedTokens = earnedTokens;
    }

    public int getBoughtTokens() {
        return boughtTokens;
    }

    public void setBoughtTokens(int boughtTokens) {
        this.boughtTokens = boughtTokens;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }
}
