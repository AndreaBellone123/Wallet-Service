package com.devied.walletservice.payment;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.CartData;
import com.devied.walletservice.data.ProductData;
import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.*;
import com.devied.walletservice.model.Payout;
import com.devied.walletservice.model.*;
import com.devied.walletservice.repository.ProductDataRepository;
import com.devied.walletservice.repository.TransactionDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.service.CartDataService;
import com.devied.walletservice.service.PaymentMethodService;
import com.devied.walletservice.service.TransactionDataService;
import com.devied.walletservice.util.Email;
import com.devied.walletservice.util.PaypalParameters;
import com.devied.walletservice.util.YAMLConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.payouts.Currency;
import com.paypal.payouts.PayoutItem;
import com.paypal.payouts.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class PaypalServiceImpl implements PaypalService {

    private final RestTemplate restTemplate;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    TransactionDataRepository transactionDataRepository;

    @Autowired
    PayPalHttpClient client;

    @Autowired
    UserConverter userConverter;

    @Autowired
    CartDataService cartDataService;

    @Autowired
    PaymentMethodService paymentMethodService;

    @Autowired
    TransactionDataService transactionDataService;

    @Autowired
    ProductDataRepository productDataRepository;

    @Autowired
    YAMLConfig yamlConfig;

    public PaypalServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Payer getPayerInformation(String email) throws UserNotFoundException {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

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
            ProductData productData = productDataRepository.findById(cartItem.getId()).orElseThrow(ProductNotFoundException::new);
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
        APIContext apiContext = new APIContext(yamlConfig.getClientId(), yamlConfig.getSecret(), yamlConfig.getMode());
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
        APIContext apiContext = new APIContext(yamlConfig.getClientId(), yamlConfig.getSecret(), yamlConfig.getMode());
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

        ObjectMapper mapper = new ObjectMapper();
        PaypalParameters params = mapper.convertValue(checkout.getParameters(), PaypalParameters.class);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(params.getPayerId());

        Payment payment = new Payment().setId(params.getPaymentId());

        APIContext apiContext = new APIContext(yamlConfig.getClientId(), yamlConfig.getSecret(), yamlConfig.getMode());

        payment.execute(apiContext, paymentExecution);

        cartDataService.finalState(name);

    }

    public User getUser(PaypalAccessToken paypalAccessToken, String username) throws UserNotFoundException, PaypalUserNotFoundException, DuplicatePaymentMethodException {


        String url = "https://api.sandbox.paypal.com/v1/identity/oauth2/userinfo?schema=paypalv1.1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paypalAccessToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<PaypalUser> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                PaypalUser.class,
                1
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Request Successful.");
            System.out.println(response.getBody());
        }

        PaypalUser paypalUser1 = response.getBody();
        if (Objects.isNull(paypalUser1) || Objects.isNull(paypalUser1.getEmails()) || paypalUser1.getEmails().size() == 0) {
            throw new PaypalUserNotFoundException();
        }

        Email email = paypalUser1.getEmails().get(0);
        UserData userData = userDataRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);

        if (userData == null) {
            UserData userData1 = new UserData();
            userData1.setEmail(username);
            PaypalMethod paypalMethod = new PaypalMethod();
            paypalMethod.setEmail(email.getValue());
            List<PaymentMethod> paymentMethods = userData1.getPaymentMethods();
            paymentMethods.add(paypalMethod);
            userData1.setPaymentMethods(paymentMethods);

            userDataRepository.save(userData1);
        } else {
            PaypalMethod paypalMethod = new PaypalMethod();
            paypalMethod.setEmail(email.getValue());
            List<PaymentMethod> paymentMethods = userData.getPaymentMethods();
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getMethod().equals("paypal")) {
                    paypalMethod = (PaypalMethod) paymentMethod;
                    if (paypalMethod.getEmail().equals(email.getValue())) {
                        throw new DuplicatePaymentMethodException();
                    }
                }
            }
            userData.getPaymentMethods().add(paypalMethod);

            userDataRepository.save(userData);
        }

        return userConverter.convert(userData);
    }

    public void cashOut(String email) throws UserNotFoundException, PaymentMethodNotAllowedException {

        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        int earned = userData.getEarned();
        double cashoutMultiplierDouble = earned / 1000;
        int cashoutMultiplierInt = (int) cashoutMultiplierDouble;
        double cashoutAmount = 50.00 * cashoutMultiplierInt;

        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();
        PaymentMethod paymentMethod = paymentMethodService.getPayOutMethod(email);
        PaypalMethod paypalMethod = null;

        if (!(paymentMethod.getMethod().equals("paypal"))) {
            throw new PaymentMethodNotAllowedException();
        }
        paypalMethod = (PaypalMethod) paymentMethod;

        final PaypalMethod finalPaypalMethod = paypalMethod;
        List<PayoutItem> items = IntStream
                .range(0, 1)
                .mapToObj(index -> new PayoutItem()
                        .senderItemId("Test_txn_" + index)
                        .note("Your 50€ Payout!")
                        .receiver(finalPaypalMethod.getEmail())
                        .amount(new Currency()
                                .currency("EUR")
                                .value(String.format(Locale.US, "%.2f", cashoutAmount))))
                .collect(Collectors.toList());

        CreatePayoutRequest request = new CreatePayoutRequest().senderBatchHeader(new SenderBatchHeader()
                .senderBatchId("Test_sdk_" + RandomStringUtils.randomAlphanumeric(7))
                .emailMessage("SDK payouts test txn")
                .emailSubject("This is a test transaction from SDK")
                .note("Enjoy your Payout!!")
                .recipientType("EMAIL"))
                .items(items);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<CreatePayoutResponse> response = client.execute(new PayoutsPostRequest().requestBody(request));

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            CreatePayoutResponse payouts = response.result();
            System.out.println("Payouts Batch ID: " + payouts.batchHeader().payoutBatchId());
            payouts.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                System.out.println("Something went wrong client-side");
            }
        }

        Payout payout = new Payout();
        payout.setSenderBatchId(request.senderBatchHeader().senderBatchId());
        payout.setAmount(request.items().get(0).amount().value());
        payout.setReceiver(request.items().get(0).receiver());

        TransactionData transactionData = new TransactionData();
        transactionData.setPayout(payout);
        transactionDataRepository.save(transactionData);
    }
}

