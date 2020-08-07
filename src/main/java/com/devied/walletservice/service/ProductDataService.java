package com.devied.walletservice.service;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public interface ProductDataService {
    //TODO togliere parte forntend
    ResponseEntity<Product> addProduct(ProductData productData, String email);

    ResponseEntity<ProductData> updateProduct(String pid, ProductData productData, String email) throws Exception;

    ResponseEntity<Product> deleteProduct(String pid) throws Exception;

    ResponseEntity<ArrayList<Product>> getProducts();

    ProductData findProduct(String id) throws Exception;
}
