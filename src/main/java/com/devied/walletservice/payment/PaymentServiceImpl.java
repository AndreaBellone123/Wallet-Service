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

        PaymentExecution paymentExecution= new PaymentExecution();
        paymentExecution.setPayerId(checkout.getPayerId());

        Payment payment = new Payment().setId(checkout.getPaymentId());

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        payment.execute(apiContext, paymentExecution);

        cartDataService.finalState(name);

    }
}