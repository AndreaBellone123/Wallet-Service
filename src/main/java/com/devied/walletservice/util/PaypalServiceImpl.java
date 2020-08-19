package com.devied.walletservice.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PaypalServiceImpl implements PaypalService {

    private final RestTemplate restTemplate;

    public PaypalServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getEmail() {

        PaypalUser paypalUser;
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
            return response.getBody();

        }

        return String.valueOf(response.getStatusCode());
    }
}
