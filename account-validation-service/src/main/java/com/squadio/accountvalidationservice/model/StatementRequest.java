package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jamesoladimeji
 * @created 20/12/2021 - 5:48 AM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class StatementRequest {
    @NotNull(message = "account ID cannot null")
    @NotBlank(message = "account ID cannot be blank")
    private String accountId;

}
