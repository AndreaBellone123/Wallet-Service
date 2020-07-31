package com.devied.walletservice.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("users")
public class UserData {
    @Id
    private String id;
    private String email;
    private int earnedToken;
    private int boughtToken;

    public UserData() {
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
