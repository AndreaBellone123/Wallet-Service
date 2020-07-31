package com.devied.walletservice.repository;

import com.devied.walletservice.data.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepository extends MongoRepository<UserData, String> {
}
