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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

        if (donatingUser.getTotal() < amount) {

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

        if (donatingUser.getBought() < amount) {

            throw new InsufficientFundsException();

        } else {

            streamingUser.setEarned(streamingUser.getEarned() + amount);

            donatingUser.setBought(donatingUser.getBought() - amount);
        }

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
    public void DeviedCashOut(String email) throws UserNotFoundException {

        //TODO controllo sul metodo di pagamento e sulla quantita' di token ricevuti dalle donazioni
        paymentService.PaypalCashOut(email);
        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        userData.setEarned(userData.getEarned() - 1000);
        userDataRepository.save(userData);

    }

    @Override
    public User createWallet(String name) {

        UserData userData = new UserData();
        userData.setEmail(name);
        return userConverter.convert(userData);
    }

    @Override
    public User addPaymentMethod(PaymentMethod paymentMethod, String name) throws UserNotFoundException {

        UserData userData = userDataRepository.findByEmail(name).orElseThrow(UserNotFoundException::new);
        userData.getPaymentMethods().add(paymentMethod);
        userDataRepository.save(userData);
        return userConverter.convert(userData);
    }

    @Override
    public User updateDefaultMethod(String id, PaymentMethod prototype, String name) throws UserNotFoundException, PaymentMethodNotFoundException {

        UserData userData = userDataRepository.findByEmail(name).orElseThrow(UserNotFoundException::new);
        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();

        PaymentMethod found = userData.getPaymentMethods()
                .stream()
                .filter(paymentMethod -> {
                   return paymentMethod.getUuid().equals(id);
                })
                .findFirst()
                .orElseThrow(PaymentMethodNotFoundException::new);

       found.setPayInMethod(prototype.isPayInMethod());
       found.setPayOutMethod(prototype.isPayOutMethod());

        userData.getPaymentMethods()
                .stream()
                .filter(paymentMethod -> !paymentMethod.getUuid().equals(id))
                .forEach(paymentMethod -> {

                    if (prototype.isPayInMethod()) {
                        paymentMethod.setPayInMethod(false);
                    }
                    if (prototype.isPayOutMethod()) {
                        paymentMethod.setPayOutMethod(false);
                    }
                });

        userDataRepository.save(userData);
        return userConverter.convert(userData);

    }

    @Override
    public User deletePaymentMethod(String id, String name) throws UserNotFoundException, PaymentMethodNotFoundException {

        UserData userData = findByEmail(name);

        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();

        for (PaymentMethod paymentMethod : paymentMethods) {

            if (id.equals(paymentMethod.getUuid())) {

                userData.getPaymentMethods().remove(paymentMethod);
                userDataRepository.save(userData);
                return userConverter.convert(userData);
            }
        }

        throw new PaymentMethodNotFoundException();

    }

}
