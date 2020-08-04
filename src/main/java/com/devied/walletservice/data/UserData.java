package com.devied.walletservice.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class UserData {
    @Id
    private String id;
    private String email;
    private int earnedTokens;
    private int boughtTokens;
    private boolean isAdmin;
    private double availableFunds;

    public UserData(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
