package com.devied.walletservice.repository;

import com.devied.walletservice.data.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDataRepository extends MongoRepository<UserData, String> {

    public Optional<UserData> findByEmail(String email);
}
