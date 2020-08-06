package com.devied.walletservice.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@RequiredArgsConstructor
@Document("users")
public class UserData {
    @Id
    private String id;
    private final String email;
    private int earnedTokens;
    private int boughtTokens;
    private boolean isAdmin;
    private double availableFunds;

}
