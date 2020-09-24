package com.devied.walletservice.service;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.DonationData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.*;
import com.devied.walletservice.event.CustomSpringEvent;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.model.PaypalMethod;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.DonationDataRepository;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


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


    @Override
    public UserData findByEmail(String email) throws UserNotFoundException {

        return userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateWallet(String email) throws Exception {

        if(email == null){
            throw new UserUnauthorizedException();
        }

        UserData userData = findByEmail(email);
        CartData cartData = cartDataService.findCurrent(email);

        ProductData productdata = productDataRepository.findById(cartData.getItemsList().get(0).getId()).orElseThrow(ProductNotFoundException::new);
        userData.setBought(userData.getBought() + productdata.getAmount() * cartData.getItemsList().get(0).getQuantity());
        userData.setTotal(userData.getBought() + userData.getEarned());
        userDataRepository.save(userData);
    }

    @Override
    public User getWallet(String email) throws Exception {

        if(email.equals("")){
            throw new UserUnauthorizedException();
        }

        UserData userData = findByEmail(email);

        if (userData == null) {

            throw new WalletNotFoundException();
        }
        return userConverter.convert(userData);
    }

    @Override
    public User donate(String donorEmail, String streamerEmail, int amount) throws Exception {

        if(donorEmail == null){
            throw new UserUnauthorizedException();
        }

        UserData donatingUser = findByEmail(donorEmail);

        UserData streamingUser = findByEmail(streamerEmail);

        if (userDataRepository.findByEmail(streamerEmail) == null) {

            throw new UserNotFoundException();
        }

        if (amount <= 0) {

            throw new AmountNotAcceptableException();
        }

        if (donatingUser.getId().equals(streamingUser.getId())) {

            throw new SameUserDonationException();
        }

        if (donatingUser.getBought() < amount) {

            throw new InsufficientFundsException();

        }

        streamingUser.setEarned(streamingUser.getEarned() + amount);

        donatingUser.setBought(donatingUser.getBought() - amount);


        userDataRepository.save(streamingUser);

        userDataRepository.save(donatingUser);

        DonationData donationData = new DonationData();
        donationData.setAmount(amount);
        donationData.setDonor(donatingUser.getEmail());
        donationData.setStreamer(streamingUser.getEmail());
        donationDataRepository.save(donationData);

        CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, donationData);
        applicationEventPublisher.publishEvent(customSpringEvent);

        System.out.println(Thread.currentThread().getName());

        return userConverter.convert(donatingUser);

    }

    @Override
    public void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException {

        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        double cashOutMultiplierDouble = userData.getEarned() / 1000;
        int cashOutMultiplierInt = (int) cashOutMultiplierDouble;
        int cashOutAmount = 1000 * cashOutMultiplierInt;
        userData.setEarned(userData.getEarned() - cashOutAmount);
        userDataRepository.save(userData);

    }

    @Override
    public User createWallet(String email) throws SameUserException, UserUnauthorizedException {
        if(email == null){
            throw new UserUnauthorizedException();
        }
        List<UserData> userDataList = userDataRepository.findAll();
        for (UserData userData : userDataList) {
            if (userData.getEmail().equals(email)) {
                throw new SameUserException();
            }
        }
        UserData newUserData = new UserData();
        newUserData.setEmail(email);
        userDataRepository.save(newUserData);

        return userConverter.convert(newUserData);
    }

    @Override
    public List<PaymentMethod> addPaymentMethod(PaymentMethod prototype, String email) throws UserNotFoundException, PaymentMethodNotFoundException, JsonProcessingException, UserUnauthorizedException {
        if(email == null){
            throw new UserUnauthorizedException();
        }
        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        List<PaymentMethod> paymentMethodList = userData.getPaymentMethods();

        if (!(prototype.getMethod().equals("paypal"))) {
            throw new PaymentMethodNotFoundException();
        }
        PaypalMethod paypalMethod = (PaypalMethod) prototype;
        paypalMethod.setUuid(UUID.randomUUID().toString().replace("-", ""));
        paymentMethodList.add(paypalMethod);

        userData.getPaymentMethods()
                .stream()
                .filter(paymentMethod -> !paymentMethod.getUuid().equals(paypalMethod.getUuid()))
                .forEach(paymentMethod -> {
                    if (paypalMethod.isPayInMethod() && paymentMethod.isPayInMethod()) {
                        paymentMethod.setPayInMethod(!(paymentMethod.isPayInMethod()));
                    }
                    if (paypalMethod.isPayOutMethod() && paymentMethod.isPayOutMethod()) {
                        paymentMethod.setPayOutMethod(!(paymentMethod.isPayOutMethod()));
                    }
                });

        userDataRepository.save(userData);
        return userData.getPaymentMethods();
    }

    @Override
    public List<PaymentMethod> updateDefaultMethod(String id, PaymentMethod prototype, String email) throws UserNotFoundException, PaymentMethodNotFoundException, UserUnauthorizedException {

        if(email == null){
            throw new UserUnauthorizedException();
        }

        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        PaymentMethod found = userData.getPaymentMethods()
                .stream()
                .filter(paymentMethod -> paymentMethod.getUuid().equals(id))
                .findFirst()
                .orElseThrow(PaymentMethodNotFoundException::new);

        found.setPayInMethod(prototype.isPayInMethod());
        found.setPayOutMethod(prototype.isPayOutMethod());

        userData.getPaymentMethods()
                .stream()
                .filter(paymentMethod -> !paymentMethod.getUuid().equals(id))
                .forEach(paymentMethod -> {

                    if (prototype.isPayInMethod() && paymentMethod.isPayInMethod()) {
                        paymentMethod.setPayInMethod(!(paymentMethod.isPayInMethod()));
                    }
                    if (prototype.isPayOutMethod() && paymentMethod.isPayOutMethod()) {
                        paymentMethod.setPayOutMethod(!(paymentMethod.isPayOutMethod()));
                    }

                });

        userDataRepository.save(userData);
        return userData.getPaymentMethods();

    }

    @Override
    public List<PaymentMethod> deletePaymentMethod(String id, String email) throws UserNotFoundException, PaymentMethodNotFoundException, UserUnauthorizedException {

        if(email == null){
            throw new UserUnauthorizedException();
        }

        UserData userData = findByEmail(email);

        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();

        for (PaymentMethod paymentMethod : paymentMethods) {

            if (id.equals(paymentMethod.getUuid())) {

                userData.getPaymentMethods().remove(paymentMethod);
                userDataRepository.save(userData);
                return userData.getPaymentMethods();
            }
        }

        throw new PaymentMethodNotFoundException();

    }

}
