package com.devied.walletservice.service;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.CartInForbiddenStatusException;
import com.devied.walletservice.error.EmptyCartException;
import com.devied.walletservice.error.NoCartsAvailableException;
import com.devied.walletservice.error.PaymentMethodNotFoundException;
import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.model.PaymentMethod;
import com.devied.walletservice.payment.PaymentService;
import com.devied.walletservice.repository.CartDataRepository;
import com.devied.walletservice.util.CartStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CartDataServiceImpl implements CartDataService {

    @Autowired
    CartDataRepository cartDataRepository;

    @Autowired
    TransactionDataService transactionDataService;

    @Autowired
    ProductDataService productDataService;

    @Autowired
    CartDataService cartDataService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserDataService userDataService;

    @Autowired
    PaymentMethodService paymentMethodService;

    @Override
    public CartData findCurrent(String email) throws NoCartsAvailableException {

       /*if (cartDataRepository.findTopByEmailOrderByDateDesc(email) == null ){

           throw new NoCartsAvailableException();

       }*/

        return cartDataRepository.findTopByEmailOrderByDateDesc(email);

    }

    @Override
    public CartData patchCurrent(String email, List<CartItem> cartItems, PaymentMethod paymentMethod) throws Exception {

        CartData cartData = findCurrent(email); // Fixed null pointer exception

        if (cartData == null) {

            cartData = new CartData();
            cartData.setEmail(email);
        }

        Set<CartItem> itemsSet = new HashSet<>(cartData.getItemsList()); // Non permette duplicati(controllo HashCode)
        itemsSet.removeAll(cartItems);
        itemsSet.addAll(cartItems);

        cartData.setItemsList(new ArrayList<>(itemsSet));

        if(cartData.getItemsList().isEmpty()){

            throw new EmptyCartException();
        }

        cartData.setSubtotal(0);

        for (CartItem i : itemsSet) {

            ProductData productData = productDataService.findProduct(i.getId());
            cartData.setSubtotal(productData.getPrice() * i.getQuantity() + cartData.getSubtotal());
        }
        UserData userData = userDataService.findByEmail(email);

        if (paymentMethod != null) {
            boolean found = false;
            for (PaymentMethod selectedPaymentMethod : userData.getPaymentMethods()) {
                if (selectedPaymentMethod.getUuid().equals(paymentMethod.getUuid())) {
                    cartData.setPaymentMethod(selectedPaymentMethod);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new PaymentMethodNotFoundException();
            }
        } else {
            if (cartData.getPaymentMethod() == null) {
                cartData.setPaymentMethod(paymentMethodService.getPayInMethod(email));
            }
        }

        cartDataRepository.save(cartData);
        return cartData;
    }

    @Override
    public void emptyCart(String email, Checkout checkout) throws Exception {

        CartData cartData = findCurrent(email);
        transactionDataService.saveTransaction(email, checkout);
        cartDataRepository.delete(cartData);
    }

    @Override
    public void updateState(CartData cartData) throws Exception {
        if (!cartData.getStatus().equals(CartStatus.Prepared)) {
            throw new CartInForbiddenStatusException();
        }

        cartData.setStatus(CartStatus.Pending);
        cartDataRepository.save(cartData);
    }

    @Override
    public void finalState(String name) throws Exception {

        CartData cartData = cartDataService.findCurrent(name);
        if (!cartData.getStatus().equals(CartStatus.Pending)) {
            throw new CartInForbiddenStatusException();
        }
        cartData.setStatus(CartStatus.Completed);
        cartDataRepository.save(cartData);
    }

    @Override
    public void checkoutCurrent(Checkout checkout, String name) throws Exception {

        CartData cartData = cartDataService.findCurrent(name);
        paymentService.completeCheckout(name, checkout,cartData);
        userDataService.updateWallet(name);
        emptyCart(name, checkout);
    }
}
