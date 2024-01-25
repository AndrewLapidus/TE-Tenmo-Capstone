package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;

import java.math.BigDecimal;

public interface AccountDao {

    public BalanceDTO getBalance(String username);

    public BalanceDTO updateBalance(String loggedIn, String recipiant, BigDecimal amount);
}
