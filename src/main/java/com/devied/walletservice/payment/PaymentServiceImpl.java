package com.devied.walletservice.payment;

import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.ProductNotFoundException;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.CartItem;
import com.devied.walletservice.model.Checkout;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.service.CartDataService;
import com.devied.walletservice.service.TransactionDataService;
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
    private static final String CLIENT_ID = "AVNSlr4OhE8kAJVt82ygsLq7yD64O8eYIwP2n1Q790DtcCzkE1-4uyfQrtR1u1Ysz_Hlaz7HdikaIFoQ";
    private static final String CLIENT_SECRET = "EK7hItd2kMKDv8zJ4-NFxSCk1myGYuP-JHXNj4SlqUGB42krSotKiHOm_jixeR9bjfp4TfCVu1k3PKbs";
    private static final String MODE = "sandbox";
    // https://www.sandbox.paypal.com/connect/?flowEntry=lg&client_id=AeWzs5E743fXTvTCNrRzzaMky1M1DxJ_Pb8xcEj_-hnHnIqiDmuC24YBILsqXdQef-pjp7MFFlhuK31N&response_type=code&scope=email&redirect_uri=https%253A%252F%252Flive.sandbox.devied.com&newUI=Y
    // C21AAFJ0hSxvjns4qX_nPFo07MSTr0SntFJrLzOuMzJhzyD8oEfs0Fnm99GkZ4-ECLyHwTj_qlbm9msgMpjG0Zu985bShcKsg - AUTH CODE
    // A23AAFNd7CEl5VRve5ATTutVKgGJSjM5hrtQxwPKBbFyVvxx4XRXtzIhqfr_896hl8CxAIdbr4jRV47AyEh6omSN3wozI9VbQ - ACCESS TOKEN
    // R23AAFppJy0tcdkB0FOcVbZ4zUsd9WttP06T7ATVEh03hWyxQmUN0M7fa_5ovp-IDkE3xFIBUTq9SP8lIgZmEKXGs7pkQdxAuopAxTCl0oEeD2rdxM4_-1_2iooR-VbHrdTgN9X3CtQbxWiUmf1yw - REFRESH TOKEN

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    CartDataService cartDataService;

    @Autowired
    TransactionDataService transactionDataService;

    @Autowired
    ProductDataRepository productDataRepository;


    public Payer getPayerInformation(String email) throws UserNotFoundException {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        UserData userData = userDataRepository.findByEmail(email).orElseThrow( () -> new UserNotFoundException());

        payerInfo.setEmail(userData.getEmail());
        payer.setPayerInfo(payerInfo);

        return payer;
    }

    public RedirectUrls getRedirectURLs() {

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/paypal_html/cancel.html");
        redirectUrls.setReturnUrl("http://localhost:8080/paypal_html/review_payment.html");

        return redirectUrls;

    }

    public List<Transaction> getTransactionInformation(CartData orderDetail) throws Exception {

        System.out.println(orderDetail.setSubtotal());

        Details details = new Details();
        details.setSubtotal(orderDetail.setSubtotal());
        details.setTax(orderDetail.setTax());
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(orderDetail.formatTotal());
        System.out.print(amount.getTotal());
        amount.setDetails(details);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getId());
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        for (CartItem cartItem : orderDetail.getItemsList()) {
            Item item = new Item();
            item.setCurrency(orderDetail.getCurrency());
            item.setName(orderDetail.getId());
            ProductData productData = productDataRepository.findById(cartItem.getId()).orElseThrow(() -> new ProductNotFoundException());
            item.setPrice(productData.setPrice());
            item.setTax(orderDetail.setTax());
            item.setQuantity(String.valueOf(cartItem.getQuantity()));
            items.add(item);
        }

        itemList.setItems(items);
        transaction.setItemList(itemList);
        List<Transaction> listTransaction = new ArrayList<>();
        System.out.println(transaction.getAmount());
        System.out.println(transaction.getItemList());
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
    public Checkout initialCheckout(String name, CartData cartData) throws Exception {

        cartDataService.updateState(cartData);

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

        Checkout checkout = new Checkout();
        assert approvedPayment != null;
        checkout.setUrl(getApprovalLink(approvedPayment));
        transactionDataService.createTransaction(checkout.getUrl(), name);

        return checkout;
    }

    @Override
    public void completeCheckout(String name, Checkout checkout) throws Exception {

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(checkout.getPayerId());

        Payment payment = new Payment().setId(checkout.getPaymentId());

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        payment.execute(apiContext, paymentExecution);

        cartDataService.finalState(name);

    }
}