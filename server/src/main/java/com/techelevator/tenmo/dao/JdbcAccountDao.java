package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public BalanceDTO getBalance(String username) {

        String sql = "SELECT username, balance\n" +
                "FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE username = ?;";
        BalanceDTO balanceDTO = null;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if(results.next()){
                String user = results.getString("username");
                BigDecimal balance = results.getBigDecimal("balance");

                balanceDTO = new BalanceDTO();
                balanceDTO.setUsername(user);
                balanceDTO.setBalance(balance);
                System.out.println(user);
                System.out.println(balance);
                return balanceDTO;
            }
        }catch (DataAccessException e){
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }
        return null;
    }

    @Override
    public BalanceDTO updateBalance(String loggedIn, String recipiant, BigDecimal amount) {

//        System.out.println(username);// fix later
//        System.out.println(amount);

        String sqlFrom = "UPDATE account SET balance = balance - ? WHERE account.account_id = (SELECT account.account_id " +
                "FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?);";

        jdbcTemplate.update(sqlFrom, amount, loggedIn);


        String sqlTo = "UPDATE account SET balance = balance + ? WHERE account.account_id = (SELECT account.account_id " +
                "FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?);";

        jdbcTemplate.update(sqlTo, amount, recipiant);

        //test int postgres
        String sql2 = "INSERT INTO transfer (transfer_amount, from_id, to_id) VALUES ( " +
                "?, (SELECT account.account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?)," +
                " (SELECT account.account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?));";

        jdbcTemplate.update(sql2, amount, loggedIn, recipiant);

        return getBalance(loggedIn);
    }
}
