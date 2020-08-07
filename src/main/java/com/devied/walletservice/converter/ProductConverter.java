package com.devied.walletservice.converter;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;

public interface ProductConverter {

    Product convert(ProductData productData);
}
