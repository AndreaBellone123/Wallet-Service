package com.devied.walletservice.util;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class YAMLConfig {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment environment = "sandbox".equals(mode)
                ? new PayPalEnvironment.Sandbox(clientId, secret)
                : new PayPalEnvironment.Live(clientId, secret);
        return new PayPalHttpClient(environment);
    }

}
