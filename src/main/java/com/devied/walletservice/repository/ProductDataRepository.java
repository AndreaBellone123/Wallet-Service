package com.devied.walletservice.repository;

import com.devied.walletservice.data.ProductData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDataRepository extends MongoRepository<ProductData, String> {

}
