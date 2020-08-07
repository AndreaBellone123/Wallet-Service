package com.devied.walletservice.service;

import com.devied.walletservice.data.TransactionData;

public interface TransactionDataService {

    void saveTransaction(String email) throws Exception;

    void createTransaction(String url, String name);

    TransactionData findByUrl(String url);

    TransactionData findByEmail(String email);
}
