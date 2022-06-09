package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    boolean postTransfer(Transfer transfer);

    boolean decreaseBalance(BigDecimal amountToDecrease, Long accountId);
}
