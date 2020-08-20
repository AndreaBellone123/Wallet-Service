package com.devied.walletservice.service;

import com.devied.walletservice.data.UserData;
import com.devied.walletservice.error.UserNotFoundException;
import com.devied.walletservice.model.PaypalUser;
import com.devied.walletservice.model.User;

<<<<<<< HEAD
import java.io.IOException;
=======
import java.net.URISyntaxException;
>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf

public interface UserDataService {

    UserData findByEmail(String email) throws UserNotFoundException;

    void updateWallet(String email) throws Exception;

    // ResponseEntity<User> buyProduct(String email, String pid) throws Exception;

    User getWallet(String email) throws Exception;

    User donate(String name,String sid,int amount) throws Exception;

<<<<<<< HEAD
    User createWallet(String name) throws IOException;
=======
    User createWallet(String name);

>>>>>>> 74e501d269eb6226eda607a69ecdfeac5a81a2cf
}
