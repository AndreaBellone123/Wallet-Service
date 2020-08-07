package com.devied.walletservice.service;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.repository.ProductDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ProductDataServiceImpl implements ProductDataService {
    //TODO togliere parte forntend
    @Autowired
    ProductDataRepository productDataRepository;

    @Override
    public ResponseEntity<Product> addProduct(ProductData productData, String email) {

        ProductData productData1 = new ProductData();
        productData1.setName(productData.getName());
        productData1.setDiscount(productData.getDiscount());
        productData1.setAmount(productData.getAmount());
        productData1.setPrice(productData.getPrice());
        productDataRepository.insert(productData1);
        Product product = new Product();
        product.setAmount(productData1.getAmount());
        product.setName(productData1.getName());
        product.setDiscount(productData1.getDiscount());
        product.setPrice(productData1.getPrice());
        return null;
    }

    @Override
    public ResponseEntity<ProductData> updateProduct(String pid, ProductData productData, String email) throws Exception {
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));
        productData1.setDiscount(productData.getDiscount());
        productData1.setName(productData.getName());
        productData1.setPrice(productData.getPrice());
        productData1.setAmount(productData.getAmount());
        final ProductData updatedProductData = productDataRepository.save(productData1);
        var headers = new HttpHeaders();
        headers.add("Product Updated", "Products Controller");
        return ResponseEntity.accepted().headers(headers).body(updatedProductData);
    }

    @Override
    public ResponseEntity<Product> deleteProduct(String pid) throws Exception {
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));
        Product product = new Product();
        product.setAmount(productData1.getAmount());
        product.setName(productData1.getName());
        product.setDiscount(productData1.getDiscount());
        product.setPrice(productData1.getPrice());
        productDataRepository.delete(productData1);
        var headers = new HttpHeaders();
        headers.add("Product Deleted", "Products Controller");
        return ResponseEntity.accepted().headers(headers).body(product);
    }

    @Override
    public ResponseEntity<ArrayList<Product>> getProducts() {

        ArrayList<ProductData> listaProductsData = (ArrayList<ProductData>) productDataRepository.findAll();

        ArrayList<Product> listaProducts = new ArrayList<Product>();

        for (ProductData productData : listaProductsData) {
            Product product = new Product();
            product.setName(productData.getName());
            product.setPrice(productData.getPrice());
            product.setDiscount(productData.getDiscount());
            product.setAmount(productData.getAmount());
            product.setId(productData.getId());
            listaProducts.add(product);
        }
        var headers = new HttpHeaders();
        headers.add("List of currently available products", "Products Controller");
        return ResponseEntity.accepted().headers(headers).body(listaProducts);
    }

    @Override
    public ProductData findProduct(String id) throws Exception {

        return productDataRepository.findById(id).orElseThrow(() -> new Exception("No Products Found"));
    }
}
