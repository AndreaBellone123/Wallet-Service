package com.devied.walletservice.api;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.repository.ProductDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class Products {

    @Autowired
    ProductDataRepository productDataRepository;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<Product> addProduct(@RequestBody ProductData productData,Authentication auth) throws Exception {

        ProductData productData1 = new ProductData(productData.getName(), productData.getTokens(), productData.getDiscount(), productData.getPrice());
        productDataRepository.insert(productData1);
        Product product = new Product();
        product.setTokens(productData1.getTokens());
        product.setName(productData1.getName());
        product.setDiscount(productData1.getDiscount());
        product.setPrice(productData1.getPrice());
        var headers = new HttpHeaders();
        headers.add("Product Added", "Wallet Controller");
        return ResponseEntity.accepted().headers(headers).body(product);
    }

    @PutMapping( path = "/{pid}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<ProductData> updateProduct(@PathVariable(value = "pid") String pid, @RequestBody ProductData productData, Authentication auth) throws Exception {

        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));
        productData1.setDiscount(productData.getDiscount());
        productData1.setName(productData.getName());
        productData1.setPrice(productData.getPrice());
        productData1.setTokens(productData.getTokens());
        final ProductData updatedProductData = productDataRepository.save(productData1);
        var headers = new HttpHeaders();
        headers.add("Product Updated", "Wallet Controller");
        return ResponseEntity.accepted().headers(headers).body(updatedProductData);
    }

    @DeleteMapping(path = "/{id}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "id") String id) throws Exception {

        ProductData productData1 = productDataRepository.findById(id).orElseThrow(() -> new Exception("No Products Found"));
        Product product = new Product();
        product.setTokens(productData1.getTokens());
        product.setName(productData1.getName());
        product.setDiscount(productData1.getDiscount());
        product.setPrice(productData1.getPrice());
        productDataRepository.delete(productData1);
        var headers = new HttpHeaders();
        headers.add("Product Deleted", "Wallet Controller");
        return ResponseEntity.accepted().headers(headers).body(product);

    }

    @GetMapping(produces = "application/json")
    @Secured(IdentityRole.AUTHORITY_USER)
    public ResponseEntity<ArrayList<Product>> getProducts() {

        ArrayList<ProductData> listaProductsData = (ArrayList<ProductData>) productDataRepository.findAll();

        ArrayList<Product> listaProducts = new ArrayList<Product>();

        for (ProductData productData : listaProductsData) {
            Product product = new Product();
            product.setName(productData.getName());
            product.setPrice(productData.getPrice());
            product.setDiscount(productData.getDiscount());
            product.setTokens(productData.getTokens());
            listaProducts.add(product);
        }
        var headers = new HttpHeaders();
        headers.add("List of currently available products", "Wallet Controller");
        return ResponseEntity.accepted().headers(headers).body(listaProducts);

    }
}
