package com.devied.walletservice.payment;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.TransactionData;
import com.devied.walletservice.model.Payout;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.TransactionDataRepository;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.util.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.payouts.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
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
    UserConverter userConverter;

    public PaypalServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public User getUser(String token, String username) throws JsonProcessingException, UserNotFoundException {

        String url = "https://api.sandbox.paypal.com/v1/identity/oauth2/userinfo?schema=paypalv1.1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
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
            // TODO error
        }

        Email email = paypalUser1.getEmails().get(0);
        UserData userData = userDataRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        if (userData == null) {
            UserData userData1 = new UserData();
            userData1.setEmail(username);
            userData1.setPaypalEmail(email.getValue());
        }
        userData.setPaypalEmail(email.getValue());

        userDataRepository.save(userData);

        return userConverter.convert(userData);
    }

    public void cashOut(String email) throws UserNotFoundException {
        /*/TODO richiamare nostro bearer token
        String url = "https://api.sandbox.paypal.com/v1/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("AVNSlr4OhE8kAJVt82ygsLq7yD64O8eYIwP2n1Q790DtcCzkE1-4uyfQrtR1u1Ysz_Hlaz7HdikaIFoQ","EK7hItd2kMKDv8zJ4-NFxSCk1myGYuP-JHXNj4SlqUGB42krSotKiHOm_jixeR9bjfp4TfCVu1k3PKbs");//TODO passare client id e secret e prendere access token da mettere nella seconda HTTP
        //TODO CREARE PAYOUTDATA
        //TODO HTTP REQUEST POST https://api.sandbox.paypal.com/v1/payments/payouts PAYOUT DATA e salvare in repository*/

        UserData userData = userDataRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        List<PayoutItem> items = IntStream
                .range(0,1)
                .mapToObj(index -> new PayoutItem()
                        .senderItemId("Test_txn_" + index)
                        .note("Your 50€ Payout!")
                        .receiver(userData.getPaypalEmail())
                        .amount(new Currency()
                                .currency("EUR")
                                .value("50.00")))
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
            HttpResponse<CreatePayoutResponse> response = PaypalCredentials.client.execute(new PayoutsPostRequest().requestBody(request));

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

