package com.devied.walletservice.api;

import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.Product;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wallets")
public class Wallet {
    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    ProductDataRepository productDataRepository;

    @GetMapping(produces = "application/Json")
    public User getWallet() throws Exception{
        String id = "5f247ee894d33017f888c3fe";
        UserData userData = userDataRepository.findById(id).orElseThrow( () -> new Exception("No User Found"));
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setBoughtToken(userData.getBoughtToken());
        user.setEarnedToken(userData.getEarnedToken());

        return user;
    }

    @GetMapping(path = "/products", produces = "application/Json")
    public List<Product> getProductsList() {
        List<ProductData> productsDataList = productDataRepository.findAll();
        List<Product> productsList = new ArrayList<Product>();
        for (ProductData p : productsDataList) {
            Product product = new Product();
            product.setName(p.getName());
            product.setDiscount(p.getDiscount());
            product.setAmount(p.getAmount());
            product.setTokens(p.getTokens());
            productsList.add(product);
        }
        return productsList;
    }
}
