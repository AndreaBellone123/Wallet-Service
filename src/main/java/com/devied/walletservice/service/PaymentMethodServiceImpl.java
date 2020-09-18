package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    UserDataService userDataService;

    @Override
    public PaymentMethod getPayInMethod(String email) throws UserNotFoundException {

        UserData userData = userDataService.findByEmail(email);
        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.isPayInMethod()) {
                return paymentMethod;
            }
        }
        return null;
    }

    @Override
    public PaymentMethod getPayOutMethod(String email) throws UserNotFoundException {

        UserData userData = userDataService.findByEmail(email);
        List<PaymentMethod> paymentMethods = userData.getPaymentMethods();
        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.isPayOutMethod()) {
                return paymentMethod;
            }
        }
        return null;
    }
}
