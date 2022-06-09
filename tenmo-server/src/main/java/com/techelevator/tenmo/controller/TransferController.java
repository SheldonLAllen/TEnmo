package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransferController {

    private UserDao userDao;
    private TransferDao transferDao;

    public TransferController(UserDao userDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable Long id) {
        return userDao.getBalance(id);
    }

    @GetMapping("/transfers")
    public List<Transfer> getTransfers() {
        return transferDao.getTransfers();
    }

    @PostMapping("/new-transfer")
    public boolean postTransfer(@RequestBody Transfer transfer) {
        return transferDao.postTransfer(transfer);
    }

    @PutMapping("/decrease-balance")
    public boolean decreaseBalance(@RequestBody Transfer transfer) {
        return transferDao.decreaseBalance(transfer.getAmount(), transfer.getAccountFrom());
    }
}
