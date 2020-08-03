package com.devied.walletservice.api;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.identity.IdentityRole;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/wallets")
public class Wallet {
    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    ProductDataRepository productDataRepository;

    @GetMapping(produces = "application/json")
    @Secured(IdentityRole.AUTHORITY_USER)
    public ResponseEntity<User> getWallet(Authentication auth)  {

        UserData userData = userDataRepository.findByEmail(auth.getName());
        User user = new User();
        user.setEmail(auth.getName());
        user.setBoughtTokens(userData.getBoughtTokens());
        user.setEarnedTokens(userData.getEarnedTokens());
        user.setAdmin(userData.isAdmin());
        var headers = new HttpHeaders();
        headers.add("Funds currently available on your account", "Wallet Controller");
        return ResponseEntity.accepted().headers(headers).body(user);
    }

    @GetMapping(path = "/products", produces = "application/json")
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

    @PostMapping(path = "/products/{uid}", produces = "application/json", consumes = "application/json")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<Product> addProduct(@PathVariable(value = "uid") String uid, @RequestBody ProductData productData) throws Exception {

        ProductData productData1 = new ProductData(productData.getName(), productData.getTokens(), productData.getDiscount(), productData.getPrice());
        UserData userData = userDataRepository.findById(uid).orElseThrow(() -> new Exception("No Users Found"));


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

    @PutMapping("/updateProduct/{uid}/{pid}")
    @Secured(IdentityRole.AUTHORITY_ADMIN)
    public ResponseEntity<ProductData> updateProduct(@PathVariable(value = "uid") String uid,@PathVariable(value = "pid") String pid, @RequestBody ProductData productData) throws Exception {

        UserData userData = userDataRepository.findById(uid).orElseThrow(() -> new Exception("No Users Found"));
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

//    @DeleteMapping(path = "/delProduct/{uid}/{pid}")
//    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "uid") String uid, @PathVariable(value = "id") String id) throws Exception {
//
//        UserData userData = userDataRepository.findById(uid).orElseThrow(() -> new Exception("No Users Found"));
//        ProductData productData1 = productDataRepository.findById(id).orElseThrow(() -> new Exception("No Products Found"));;
//
//        if(!userData.isAdmin()) {
//            return null;
//        }
//
//        else {
//            Product product = new Product();
//            product.setTokens(productData1.getTokens());
//            product.setName(productData1.getName());
//            product.setDiscount(productData1.getDiscount());
//            product.setPrice(productData1.getPrice());
//            productDataRepository.delete(productData1);
//            var headers = new HttpHeaders();
//            headers.add("Product Deleted", "Wallet Controller");
//            return ResponseEntity.accepted().headers(headers).body(product);
//        }
//    }

    @DeleteMapping("/deleteProduct/{id}")
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

    @PutMapping("/buyProduct/{pid}")
    @Secured(IdentityRole.AUTHORITY_USER)
    public ResponseEntity<User> buyProduct(@PathVariable(value = "pid")String pid,Authentication auth) throws Exception{

        UserData userData = userDataRepository.findByEmail(auth.getName());
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));

        if(userData.getAvailableFunds() >= productData1.getPrice()) {
            userData.setBoughtTokens(userData.getBoughtTokens() + productData1.getTokens());
            userData.setAvailableFunds(userData.getAvailableFunds() - productData1.getPrice());
            userDataRepository.save(userData);
            User user = new User();
            user.setBoughtTokens(userData.getBoughtTokens());
            user.setEarnedTokens(userData.getEarnedTokens());
            user.setAvailableFunds(userData.getAvailableFunds());
            user.setEmail(auth.getName());
            var headers = new HttpHeaders();
            headers.add("Tokens bought successfully", "Wallet Controller");
            return ResponseEntity.accepted().headers(headers).body(user);
        }

        return null;

    }
}
