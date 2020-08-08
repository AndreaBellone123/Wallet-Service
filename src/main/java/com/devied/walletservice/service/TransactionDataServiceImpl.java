package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.TransactionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionDataServiceImpl implements TransactionDataService {

    @Autowired
    CartDataService cartDataService;

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    TransactionDataRepository transactionDataRepository;

    @Override
    public void saveTransaction(String email, Checkout checkout) throws Exception {

        CartData cartData = cartDataService.findCurrent(email);
        TransactionData transactionData = transactionDataRepository.findTopByEmailOrderByDateDesc(email);
        ProductData productdata = productDataRepository.findById(cartData.getItemsList().get(0).getId()).orElseThrow(() -> new Exception("No Products Found"));
        transactionData.setProductData(productdata);
        transactionData.setPaymentId(checkout.getPaymentId());
        transactionDataRepository.save(transactionData);
    }

    @Override
    public void createTransaction(String url, String name) {
        TransactionData transactionData = new TransactionData();
        transactionData.setEmail(name);
        transactionData.setUrl(url);
        transactionDataRepository.save(transactionData);
    }

    @Override
    public TransactionData findByUrl(String url) {
        return transactionDataRepository.findByUrl(url);
    }

    @Override
    public TransactionData findTopByEmailOrderByDateDesc(String email) {

        return transactionDataRepository.findTopByEmailOrderByDateDesc(email);
    }
}
