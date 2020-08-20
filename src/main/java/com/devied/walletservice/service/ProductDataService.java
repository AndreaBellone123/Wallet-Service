package com.devied.walletservice.service;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;
import java.util.ArrayList;

public interface ProductDataService {

    Product addProduct(ProductData productData, String email);

    Product updateProduct(String pid, ProductData productData, String email) throws Exception;

    Product deleteProduct(String pid) throws Exception;

    ArrayList<Product> getProducts();

    ProductData findProduct(String id) throws Exception;
}
