package com.devied.walletservice.repository;

import com.devied.walletservice.data.PayoutData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PayoutDataRepository extends MongoRepository<PayoutData,String> {
}
