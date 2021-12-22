package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 2:15 PM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class UserAccount {

    private String id;
    private String accountType;
    private String accountNumber;
    private String IBAN;
    private String balance;
    private String currency;
}
