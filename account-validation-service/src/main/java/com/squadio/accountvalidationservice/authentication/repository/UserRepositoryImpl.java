package com.squadio.accountvalidationservice.authentication.repository;


import com.squadio.accountvalidationservice.authentication.entity.AccountUser;
import com.squadio.accountvalidationservice.remoteservice.RestClientService;
import com.squadio.accountvalidationservice.remoteservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jamesoladimeji
 * @created 28/10/2021 - 4:40 PM
 * @project IntelliJ IDEA
 */
@Component
public class UserRepositoryImpl  implements UserRepository {
    @Autowired
    private RestClientService restClientService;

    @Override
    public AccountUser findByUserName(String username) {
        //get user from the repo
        User user = restClientService.FindUserByUsername(username);
        String password = "Admin".equals(username) ? "admin" : "user";
        return new AccountUser(user.getId(), user.getName(), password);
    }
}
