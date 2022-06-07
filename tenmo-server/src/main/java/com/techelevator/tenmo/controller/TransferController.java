package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransferController {

    private UserDao userDao;

    public TransferController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable Long id) {
        return userDao.getBalance(id);
    }
}