package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserLists;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private JdbcUserDao jdbcUserDao;
    private BalanceDTO balanceDTO;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    @Override
    public TransferDTO sendMoney(String currentUser, String recipient, BigDecimal amount){
        //verify Kenny loggins is not desired recipient and user is real. No imaginary friends allowed
//        System.out.println("checking if user is sending to self");
        if(currentUser.equals(recipient)){
//            System.out.println("user is sending to self");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is trying to send money to self. Please choose another user.");
        }
        System.out.println("checking balances of sender and recipiant");
        BalanceDTO loggedInUser = jdbcAccountDao.getBalance(currentUser);
        //if user doesn't exsist does it exit this code???
        BalanceDTO recipiantUser = jdbcAccountDao.getBalance(recipient);
        if(recipiantUser == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user you are trying to send to does not exist. Please choose another user.");

        }

        // verify loggins account has more or equal to ammount to send
        int compareValueGoodBalance = loggedInUser.getBalance().compareTo(amount);
        if(compareValueGoodBalance == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is trying to send more money than in account. Please adjust the amount to send.");

        }

        //verify ammount to send > 0 ie no negative or zero value
        int compareValuePositive = loggedInUser.getBalance().compareTo(new BigDecimal("0"));
        if (compareValuePositive == 0 || compareValuePositive == -1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is trying to send nothing or a negative value. Please adjust the amount to send.");

        }



        // reciever balance increases
        //sender decrease
        jdbcAccountDao.updateBalance(currentUser,recipient,amount);

        //format transferResult
        String sql = "select max (transfer_id) from transfer;";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class);
        TransferDTO transferDTO = new TransferDTO(transferId, amount, currentUser, recipient);



        return transferDTO;
    }

    @Override
    public List<UserLists> listUsers(String currentUser){
        List<UserLists> users = new ArrayList<>();
        String sql = "Select username From tenmo_user where username not like(?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUser);
        while(results.next()){
            UserLists user = new UserLists(results.getString("username"));
            users.add(user);

        }
        return users;
    }

    @Override
    public List<TransferDTO> getRecentTransaction(String currentUser){
        List<TransferDTO> transfers = new ArrayList<>();
        String sql = "select transfer_id, transfer_amount, " +
                "(select username from tenmo_user join account on tenmo_user.user_id = account.user_id where account.account_id = transfer.from_id ) as sender, " +
                "(select username from tenmo_user join account on tenmo_user.user_id = account.user_id where account.account_id = transfer.to_id) as recipient " +
                "from transfer " +
                "where from_id = (select account_id from account join tenmo_user on account.user_id = tenmo_user.user_id where username = ?) " +
                "or to_id = (select account_id from account join tenmo_user on account.user_id = tenmo_user.user_id where username = ?) "  +
                "order by transfer_id desc; ";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, currentUser, currentUser);
        while(rs.next()){
            TransferDTO transaction = new TransferDTO();
            transaction.setTransferIid(rs.getInt("transfer_id"));
            transaction.setAmount(rs.getBigDecimal("transfer_amount"));
            transaction.setLoggedIn(rs.getString("sender"));
            transaction.setRecipiant(rs.getString("recipient"));
            transfers.add(transaction);

        }
        return transfers;
    }

    public TransferDTO getTransaction(String currentUser, int id){

        List<TransferDTO> transfers = getRecentTransaction(currentUser);
        for( TransferDTO transfer : transfers){
            if(transfer.getTransferIid() == id){
                return transfer;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transfer Id is either not valid. Please choose another Id.");
    }
}
