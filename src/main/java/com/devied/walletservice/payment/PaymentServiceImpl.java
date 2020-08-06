package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.repository.UserDataRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private static final String CLIENT_ID = "AeWzs5E743fXTvTCNrRzzaMky1M1DxJ_Pb8xcEj_-hnHnIqiDmuC24YBILsqXdQef-pjp7MFFlhuK31N";
    private static final String CLIENT_SECRET = "EOwAPnAW5oVJmyU0-wphXdgofFQEYXoBPfyVaCoweb-DlyVp2Yp7rLShjYIcYDYzH3OiFsPV0WTdbxK6";
    private static final String MODE = "sandbox";

    @Autowired
    UserDataRepository userDataRepository;


    public Payer getPayerInformation(String email) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        UserData userData = userDataRepository.findByEmail(email);

        payerInfo.setEmail(userData.getEmail());
        payerInfo.setFirstName("Andrea");
        payerInfo.setLastName("Bellone");
        payer.setPayerInfo(payerInfo);

        return payer;
    }

    public RedirectUrls getRedirectURLs() {

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/paypal_html/cancel.html");
        redirectUrls.setReturnUrl("http://localhost:8080/paypal_html/review_payment.html");

        return redirectUrls;

    }

    public List<Transaction> getTransactionInformation(CartData orderDetail) {

        Details details = new Details();
        details.setSubtotal(orderDetail.getSubtotal());
        details.setTax(orderDetail.getTax());

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getId());
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setCurrency("EUR");
        item.setName(orderDetail.getId());
        item.setPrice(orderDetail.getSubtotal());
        item.setTax(orderDetail.getTax());
        item.setQuantity("1");
        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);
        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    public String getApprovalLink(Payment approvedPayment) {

        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    @Override
    public String initialCheckout(String name, CartData cartData) {
        Payer payer = getPayerInformation(name);
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(cartData);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = null;
        try {
            approvedPayment = requestPayment.create(apiContext);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return getApprovalLink(approvedPayment);
    }

    @Override
    public void completeCheckout(String name, Checkout checkout) throws PayPalRESTException {

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(checkout.getPayerId());

        Payment payment = new Payment().setId(checkout.getPaymentId());

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        payment.execute(apiContext, paymentExecution);

    }
}