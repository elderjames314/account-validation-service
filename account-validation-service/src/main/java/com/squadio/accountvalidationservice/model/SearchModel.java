package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author jamesoladimeji
 * @created 20/12/2021 - 5:53 PM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SearchModel {
    @NotNull( message = "Account Id cannot be null")
    @NotBlank( message = "Account Id cannot be null")
    private String userId;
    private Date fromDate;
    private Date toDate;
    private double fromAmount = 0.0; //if not value give is till be initialize as 0.0
    private double toAmount = 0.0;
}
