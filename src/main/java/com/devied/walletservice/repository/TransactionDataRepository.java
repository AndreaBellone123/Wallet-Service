package com.devied.walletservice.repository;

import com.devied.walletservice.data.TransactionData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionDataRepository extends MongoRepository<TransactionData,String> {

    TransactionData findByUrl(String url);

    TransactionData findTopByEmailOrderByDateDesc(String email);
}
