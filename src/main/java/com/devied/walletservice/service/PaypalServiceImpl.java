package com.devied.walletservice.service;

import com.devied.walletservice.converter.UserConverter;
import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.User;
import com.devied.walletservice.repository.UserDataRepository;
import com.devied.walletservice.util.Email;
import com.devied.walletservice.util.PaypalUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

@Service
@Transactional
public class PaypalServiceImpl implements PaypalService {

    private final RestTemplate restTemplate;

    @Autowired
    UserDataRepository userDataRepository;

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
        if (userData == null){
            UserData userData1 = new UserData();
            userData1.setEmail(username);
            userData1.setPaypalEmail(email.getValue());
        }
        userData.setPaypalEmail(email.getValue());

        userDataRepository.save(userData);

        return userConverter.convert(userData);
    }
}
