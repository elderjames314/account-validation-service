package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 12:50 PM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class NormalResponse {
    private String responseCode = "99";
    private String responseMessage = "FAIL";
    private Date created = new Date();
}
