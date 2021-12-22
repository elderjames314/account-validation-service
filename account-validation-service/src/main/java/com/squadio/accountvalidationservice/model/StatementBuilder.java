package com.squadio.accountvalidationservice.model;

/**
 * @author jamesoladimeji
 * @created 21/12/2021 - 5:46 PM
 * @project IntelliJ IDEA
 */
public class StatementBuilder {
    private String accountNumber;
    private String description;
    private String amount;
    private String date;

    public StatementBuilder setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;

    }

    public StatementBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public StatementBuilder setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public StatementBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    public Statement build() {
        return new Statement(accountNumber, description, amount, date);
    }
}
