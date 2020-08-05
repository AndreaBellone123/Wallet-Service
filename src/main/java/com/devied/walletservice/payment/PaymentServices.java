package com.devied.walletservice.payment;

import java.util.*;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.model.OrderDetail;
import com.devied.walletservice.repository.UserDataRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentServices {
    private static final String CLIENT_ID = "AeWzs5E743fXTvTCNrRzzaMky1M1DxJ_Pb8xcEj_-hnHnIqiDmuC24YBILsqXdQef-pjp7MFFlhuK31N";
    private static final String CLIENT_SECRET = "EOwAPnAW5oVJmyU0-wphXdgofFQEYXoBPfyVaCoweb-DlyVp2Yp7rLShjYIcYDYzH3OiFsPV0WTdbxK6";
    private static final String MODE = "sandbox";

    @Autowired
    UserDataRepository userDataRepository;

    public String authorizePayment(OrderDetail orderDetail)
            throws PayPalRESTException {

        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(orderDetail);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);

    }

    private Payer getPayerInformation(String email) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");



        PayerInfo payerInfo = new PayerInfo();
        UserData userData = userDataRepository.findByEmail(email);

        payerInfo.setFirstName("William")
                .setLastName("Peterson")
                .setEmail(userData.getEmail());

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/paypal_html/cancel.html");
        redirectUrls.setReturnUrl("http://localhost:8080/paypal_html/review_payment.html");

        return redirectUrls;

    }

    private List<Transaction> getTransactionInformation(OrderDetail orderDetail) {

        Details details = new Details();
        details.setSubtotal(orderDetail.getSubtotal());
        details.setTax(orderDetail.getTax());

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getProductName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setCurrency("USD");
        item.setName(orderDetail.getProductName());
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

    private String getApprovalLink(Payment approvedPayment) {

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

    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }
}