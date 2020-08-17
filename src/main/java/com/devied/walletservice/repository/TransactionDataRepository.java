package com.devied.walletservice.repository;

import com.devied.walletservice.data.TransactionData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TransactionDataRepository extends MongoRepository<TransactionData,String> {

    Optional<TransactionData> findByUrl(String url);

    Optional<TransactionData> findTopByEmailOrderByDateDesc(String email);
}
