package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserLists;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    TransferDTO sendMoney(String currentUser, String recipient, BigDecimal amount);

    List<UserLists> listUsers(String currentUser);

    List<TransferDTO> getRecentTransaction(String currentUser);

    TransferDTO getTransaction(String currentUser, int id);
}
