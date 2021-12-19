package com.squadio.accountvalidationservice.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jamesoladimeji
 * @created 07/09/2021 - 6:22 PM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ErrorReponse {
    private String message;
    private String responseCode;
    private String responseMessage;

}
