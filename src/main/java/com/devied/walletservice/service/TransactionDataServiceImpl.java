package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.TransactionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionDataServiceImpl implements TransactionDataService{

    @Autowired
    CartDataService cartDataService;

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    TransactionDataRepository transactionDataRepository;

    @Override
    public void saveTransaction(String email) throws Exception {

        CartData cartData = cartDataService.findCurrent(email);
        TransactionData transactionData = new TransactionData();
        ProductData productdata = productDataRepository.findById(cartData.getItemsList().get(0).getId()).orElseThrow(() -> new Exception("No Products Found"));
        transactionData.setProductData(productdata);
        transactionData.setEmail(email);
        transactionDataRepository.save(transactionData);
    }
}
