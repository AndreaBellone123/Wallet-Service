package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.error.ProductNotFoundException;
import com.devied.walletservice.error.TransactionNotFoundException;
import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.TransactionDataRepository;
import com.devied.walletservice.util.PaypalParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        ObjectMapper mapper = new ObjectMapper();
        PaypalParameters params = mapper.convertValue(checkout.getParameters(), PaypalParameters.class);

        CartData cartData = cartDataService.findCurrent(email);
        TransactionData transactionData = transactionDataRepository.findTopByEmailOrderByDateDesc(email).orElseThrow(() -> new TransactionNotFoundException());
        List<ProductData> productDataList = transactionData.getProductDataList();
        for (CartItem cartItem : cartData.getItemsList()) {
            ProductData productData = productDataRepository.findById(cartItem.getId()).orElseThrow(ProductNotFoundException::new);
            productData.setQuantity(cartItem.getQuantity());
            productDataList.add(productData);
        }
        transactionData.setProductDataList(productDataList);
        transactionData.setPaymentId(params.getPaymentId());
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
    public TransactionData findByUrl(String url) throws TransactionNotFoundException {
        return transactionDataRepository.findByUrl(url).orElseThrow(() -> new TransactionNotFoundException());
    }

    @Override
    public TransactionData findTopByEmailOrderByDateDesc(String email) throws TransactionNotFoundException {

        return transactionDataRepository.findTopByEmailOrderByDateDesc(email).orElseThrow(() -> new TransactionNotFoundException());
    }
}
