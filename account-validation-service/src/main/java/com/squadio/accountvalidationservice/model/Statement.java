package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 20/12/2021 - 5:45 AM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Statement {
    private String accountNumber;
    private String description;
    private String amount;
    private String date;
}
