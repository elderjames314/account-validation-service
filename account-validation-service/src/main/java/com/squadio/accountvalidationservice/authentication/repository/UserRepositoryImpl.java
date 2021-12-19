package com.squadio.accountvalidationservice.authentication.repository;


import com.squadio.accountvalidationmodule.authentication.entity.AccountUser;
import org.springframework.stereotype.Component;

/**
 * @author jamesoladimeji
 * @created 28/10/2021 - 4:40 PM
 * @project IntelliJ IDEA
 */
@Component
public class UserRepositoryImpl  implements UserRepository {
    @Override
    public AccountUser findByUserName(String username) {

        //get user from the repo

        return null;
    }
}
