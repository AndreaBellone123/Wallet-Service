package com.devied.walletservice.payment;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PaypalCredentials {

    static String clientId = "AVNSlr4OhE8kAJVt82ygsLq7yD64O8eYIwP2n1Q790DtcCzkE1-4uyfQrtR1u1Ysz_Hlaz7HdikaIFoQ";
    static String secret = "EK7hItd2kMKDv8zJ4-NFxSCk1myGYuP-JHXNj4SlqUGB42krSotKiHOm_jixeR9bjfp4TfCVu1k3PKbs";

    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    static PayPalHttpClient client = new PayPalHttpClient(environment);

}
