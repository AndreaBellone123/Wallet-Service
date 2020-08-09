package com.devied.walletservice.converter;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductConverterImpl implements ProductConverter {
    @Override
    public Product convert(ProductData productData) {

        Product product = new Product();
        product.setAmount(productData.getAmount());
        product.setDiscount(productData.getDiscount());
        product.setId(productData.getId());
        product.setName(productData.getName());
        product.setPrice(productData.getPrice());

        return product;
    }
}
