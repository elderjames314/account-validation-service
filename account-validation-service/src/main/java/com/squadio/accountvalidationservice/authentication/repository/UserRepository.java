package com.squadio.accountvalidationservice.authentication.repository;


import com.squadio.accountvalidationmodule.authentication.entity.AccountUser;

public interface UserRepository{
    AccountUser findByUserName(String username);
}
