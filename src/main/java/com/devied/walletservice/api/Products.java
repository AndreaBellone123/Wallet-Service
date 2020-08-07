package com.devied.walletservice.api;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.service.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class Products {

    @Autowired
    ProductDataService productDataService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public Product addProduct(@RequestBody ProductData productData, Authentication auth) throws Exception {

        return productDataService.addProduct(productData, auth.getName());
    }

    @PutMapping(path = "/{pid}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public Product updateProduct(@PathVariable(value = "pid") String pid, @RequestBody ProductData productData, Authentication auth) throws Exception {

        return productDataService.updateProduct(pid, productData, auth.getName());
    }

    @DeleteMapping(path = "/{id}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public Product deleteProduct(@PathVariable(value = "id") String id) throws Exception {

        return productDataService.deleteProduct(id);
    }

    @GetMapping(produces = "application/json")
    public ArrayList<Product> getProducts() {

        return productDataService.getProducts();
    }
}
