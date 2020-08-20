package com.devied.walletservice.util;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.repository.UserDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional
public class PaypalServiceImpl implements PaypalService {

    private final RestTemplate restTemplate;

    @Autowired
    UserDataRepository userDataRepository;

    public PaypalServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public PaypalUser getEmail() throws JsonProcessingException, UserNotFoundException {

        String url = "https://api.sandbox.paypal.com/v1/identity/oauth2/userinfo?schema=paypalv1.1";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("A23AAFNd7CEl5VRve5ATTutVKgGJSjM5hrtQxwPKBbFyVvxx4XRXtzIhqfr_896hl8CxAIdbr4jRV47AyEh6omSN3wozI9VbQ");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class,
                1
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Request Successful.");
            System.out.println(response.getBody());
        }

        PaypalUser paypalUser1 = new ObjectMapper().readValue(response.getBody(), PaypalUser.class);
        Email email = new ObjectMapper().readValue(response.getBody(), Email.class);
        UserData userData = userDataRepository.findByEmail(email.getValue()).orElseThrow(UserNotFoundException::new);
        userData.setPaypal_email(email.getValue());
        userDataRepository.save(userData);
        return paypalUser1;
    }
}
