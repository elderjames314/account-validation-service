package com.squadio.accountvalidationservice.remoteservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 8:25 AM
 * @project IntelliJ IDEA
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class User {
    private String id;
    private String name;
}
