package com.squadio.accountvalidationservice.authentication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 3:34 AM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AccountUser {
    private String username;
    private String id;
    private String password;
}
