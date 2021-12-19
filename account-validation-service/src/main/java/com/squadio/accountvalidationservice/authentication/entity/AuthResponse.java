package com.squadio.accountvalidationservice.authentication.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 28/10/2021 - 4:53 AM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AuthResponse {
    private String username;
    private String id;
    private String token;
}
