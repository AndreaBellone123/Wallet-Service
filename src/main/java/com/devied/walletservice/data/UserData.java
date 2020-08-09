package com.devied.walletservice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document("users")
public class UserData {

    @Id
    private String id;
    private String email;
    private int earned;
    private int bought;
    private int total;
}
