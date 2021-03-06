package com.devied.walletservice.service;

import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.error.TransactionNotFoundException;
import com.devied.walletservice.model.Checkout;

public interface TransactionDataService {

    void saveTransaction(String email, Checkout checkout) throws Exception;

    void createTransaction(String url, String name);

    TransactionData findByUrl(String url) throws TransactionNotFoundException;

    TransactionData findTopByEmailOrderByDateDesc(String email) throws TransactionNotFoundException;
}
