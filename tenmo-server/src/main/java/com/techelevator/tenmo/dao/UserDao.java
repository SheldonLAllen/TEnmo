package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    String findUsernameByAccountId(Long accountId);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    BigDecimal getBalance(Long id);

    Long getAccountIdByUserId(Long id);

//    BigDecimal requestBucks(BigDecimal bucks, Long id);
}
