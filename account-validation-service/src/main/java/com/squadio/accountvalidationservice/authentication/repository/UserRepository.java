package com.squadio.accountvalidationservice.authentication.repository;


import com.squadio.accountvalidationservice.authentication.entity.AccountUser;

public interface UserRepository{
    AccountUser findByUserName(String username);
}
