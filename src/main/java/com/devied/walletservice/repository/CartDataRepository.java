package com.devied.walletservice.repository;

import com.devied.walletservice.data.CartData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartDataRepository extends MongoRepository<CartData,String> {

   CartData findTopByEmailOrderByDateDesc(String email);
}
