package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    Transfer getTransferById(Long id);

    List<Transfer> getTransfersByAccountId(Long accountId);

    boolean postTransfer(Transfer transfer);

    boolean decreaseBalance(BigDecimal amountToDecrease, Long accountId);

    boolean increaseBalance(BigDecimal amountToIncrease, Long accountId);
}
