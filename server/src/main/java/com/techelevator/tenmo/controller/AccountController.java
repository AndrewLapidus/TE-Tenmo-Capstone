package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserLists;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BalanceDTO getBalance(Principal principal){
        return accountDao.getBalance(principal.getName());
    }


    @RequestMapping(path = "transfer/users", method = RequestMethod.GET)
    public List<UserLists> getUserList(Principal principal) {
        return transferDao.listUsers(principal.getName());
    }

    @RequestMapping(path = "/transfer/send", method = RequestMethod.PUT)
    public TransferDTO sendAmountToUser(Principal principal, @RequestBody BalanceDTO balanceDTO){
        return transferDao.sendMoney(principal.getName(), balanceDTO.getUsername() , balanceDTO.getBalance());

    }

    @RequestMapping(path = "/transfer/recent", method = RequestMethod.GET)
    public List<TransferDTO> getRecentTransactions(Principal principal){
        return transferDao.getRecentTransaction(principal.getName());

    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public TransferDTO getTransactionById(Principal principal, @PathVariable int id){
        return transferDao.getTransaction(principal.getName(), id);
    }
}
