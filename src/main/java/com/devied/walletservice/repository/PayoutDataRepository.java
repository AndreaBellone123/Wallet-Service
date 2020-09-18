package com.devied.walletservice.repository;

import com.devied.walletservice.model.Payout;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PayoutDataRepository extends MongoRepository<Payout, String> {
}
