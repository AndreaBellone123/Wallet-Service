package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.model.Checkout;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

import java.util.List;

public interface PaymentService {
    Payer getPayerInformation(String email);

    RedirectUrls getRedirectURLs();

    List<Transaction> getTransactionInformation(CartData orderDetail);

    String getApprovalLink(Payment approvedPayment);

    Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

    Checkout initialCheckout(String name, CartData cartData) throws Exception;

    void completeCheckout(String name, Checkout checkout) throws Exception;
}
