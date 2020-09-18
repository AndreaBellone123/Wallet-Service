package com.devied.walletservice.repository;

import com.devied.walletservice.data.DonationData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DonationDataRepository extends MongoRepository<DonationData, String> {
}
