package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @GetMapping("/users/{accountId}")
    public String getUsernameByAccountId(@PathVariable Long accountId) {
        return userDao.findUsernameByAccountId(accountId);
    }

    @GetMapping("/user-account/{id}")
    public Long getAccountIdByUserId(@PathVariable Long id) {
        return userDao.getAccountIdByUserId(id);
    }
}
