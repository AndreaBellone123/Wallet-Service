package com.devied.walletservice.service;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.DonationData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.*;
import com.devied.walletservice.event.CustomSpringEvent;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.User;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.repository.DonationDataRepository;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.payouts.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.auth.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;




@Service
@Transactional
@Component
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    CartDataService cartDataService;

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    DonationDataRepository donationDataRepository;

    @Autowired
    UserConverter userConverter;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    PaymentService paymentService;

    @Override
    public UserData findByEmail(String email) throws UserNotFoundException {
        return userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateWallet(String email) throws Exception {

        UserData userData = findByEmail(email);
        CartData cartData = cartDataService.findCurrent(email);
        ProductData productdata = productDataRepository.findById(cartData.getItemsList().get(0).getId()).orElseThrow(ProductNotFoundException::new);
        userData.setBought(userData.getBought() + productdata.getAmount() * cartData.getItemsList().get(0).getQuantity());
        userData.setTotal(userData.getBought() + userData.getEarned());
        userDataRepository.save(userData);
    }

    /*@Override
    public ResponseEntity<User> buyProduct(String email, String pid) throws Exception {

        UserData userData = userDataRepository.findByEmail(email);
        ProductData productData1 = productDataRepository.findById(pid).orElseThrow(() -> new Exception("No Products Found"));

        if(userData.getAvailableFunds() >= productData1.getPrice()) {
            userData.setBoughtTokens(userData.getBoughtTokens() + productData1.getAmount());
            userData.setAvailableFunds(userData.getAvailableFunds() - productData1.getPrice());
            userDataRepository.save(userData);
            User user = userConverter.convert(userData);
            var headers = new HttpHeaders();
            headers.add("Tokens bought successfully", "Wallets Controller");
            return ResponseEntity.accepted().headers(headers).body(user);
        }
        else {
            var headers = new HttpHeaders();
            headers.add("Insufficient Funds!", "Wallets Controller");
            User user = userConverter.convert(userData);
            return ResponseEntity.badRequest().headers(headers).body(user);
        }
    }*/

    @Override
    public User getWallet(String email) throws Exception {

        UserData userData = findByEmail(email);

        if (userData == null) {

            throw new WalletNotFoundException();
        }
        return userConverter.convert(userData);
    }

    @Override
    public User donate(String email, String sid, int amount) throws Exception {

        UserData donatingUser = findByEmail(email);

        UserData streamingUser = findByEmail(sid);

        if (donatingUser.getBought() < amount) {

            throw new InsufficientFundsException();
        }

        if (userDataRepository.findByEmail(sid) == null) {

            throw new UserNotFoundException();
        }

        if (amount <= 0) {

            throw new AmountNotAcceptableException();
        }

        if (donatingUser.getId().equals(streamingUser.getId())) {

            throw new SameUserException();
        }

        streamingUser.setEarned(streamingUser.getEarned() + amount);

        donatingUser.setBought(donatingUser.getBought() - amount);

        donatingUser.setTotal(donatingUser.getBought() + donatingUser.getEarned());

        streamingUser.setTotal(streamingUser.getBought() + streamingUser.getEarned());

        userDataRepository.save(streamingUser);

        userDataRepository.save(donatingUser);

        DonationData donationData = new DonationData();
        donationData.setAmount(amount);
        donationData.setDonor(donatingUser.getEmail());
        donationData.setStreamer(streamingUser.getEmail());
        donationDataRepository.save(donationData);

        CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, donationData);
        applicationEventPublisher.publishEvent(customSpringEvent);

        return userConverter.convert(donatingUser);

    }

    @Override
    public void DeviedCashOut(String email) throws UserNotFoundException {
        //TODO metodo di pagamento controllo
        paymentService.PaypalCashOut(email);
    }

}
