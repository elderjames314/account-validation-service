package com.squadio.accountvalidationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author jamesoladimeji
 * @created 19/12/2021 - 12:50 PM
 * @project IntelliJ IDEA
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AppResponse<T> {
    private NormalResponse response;
    private T data;
    private List<T> messages;
}
