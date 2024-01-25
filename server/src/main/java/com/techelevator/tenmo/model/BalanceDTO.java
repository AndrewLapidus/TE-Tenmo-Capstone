package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class BalanceDTO {

    private String username;
    private BigDecimal balance;

    public String getUsername() {
        System.out.println("getting name ");
        System.out.println(username);
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        System.out.println("get balance");
        System.out.println(balance);
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
