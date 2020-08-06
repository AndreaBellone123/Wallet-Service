package com.devied.walletservice.api;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.service.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ProductDataService productDataService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<Product> addProduct(@RequestBody ProductData productData, Authentication auth) throws Exception {

        return productDataService.addProduct(productData, auth.getName());
    }

    @PutMapping(path = "/{pid}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<ProductData> updateProduct(@PathVariable(value = "pid") String pid, @RequestBody ProductData productData, Authentication auth) throws Exception {

        return productDataService.updateProduct(pid, productData, auth.getName());
    }

    @DeleteMapping(path = "/{id}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "id") String id) throws Exception {

        return productDataService.deleteProduct(id);
    }

    @GetMapping(produces = "application/json")
    @Secured({IdentityRole.AUTHORITY_USER, IdentityRole.AUTHORITY_ADMIN})
    public ResponseEntity<ArrayList<Product>> getProducts() {

        return productDataService.getProducts();
    }
}
