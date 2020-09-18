package com.devied.walletservice.service;

import com.devied.walletservice.converter.ProductConverter;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.repository.ProductDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ProductDataServiceImpl implements ProductDataService {

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    ProductConverter productConverter;

    @Override
    public Product addProduct(ProductData productData, String email) {

        ProductData productData1 = new ProductData();
        productData1.setName(productData.getName());
        productData1.setDiscount(productData.getDiscount());
        productData1.setAmount(productData.getAmount());
        productData1.setPrice(productData.getPrice());
        productDataRepository.insert(productData1);

        return productConverter.convert(productData1);
    }

    @Override
    public Product updateProduct(String pid, ProductData productData, String email) throws Exception {
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));
        productData1.setDiscount(productData.getDiscount());
        productData1.setName(productData.getName());
        productData1.setPrice(productData.getPrice());
        productData1.setAmount(productData.getAmount());
        ProductData updatedProductData = productDataRepository.save(productData1);

        return productConverter.convert(productData1);
    }

    @Override
    public Product deleteProduct(String pid) throws Exception {
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));
        productDataRepository.delete(productData1);

        return productConverter.convert(productData1);
    }

    @Override
    public ArrayList<Product> getProducts() {

        ArrayList<ProductData> productsDataList = (ArrayList<ProductData>) productDataRepository.findAll();

        ArrayList<Product> productsList = new ArrayList<>();

        for (ProductData productData : productsDataList) {

            productsList.add(productConverter.convert(productData));
        }

        return productsList;
    }

    @Override
    public ProductData findProduct(String id) throws Exception {

        return productDataRepository.findById(id).orElseThrow(() -> new Exception("No Products Found"));
    }
}
