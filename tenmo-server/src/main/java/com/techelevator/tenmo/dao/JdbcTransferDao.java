package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfers() {
        String sql = "SELECT * FROM transfer";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        List<Transfer> transfers = new ArrayList<>();
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean postTransfer(Transfer transfer) {
        String sql = "insert into transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) values(DEFAULT, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean decreaseBalance(BigDecimal amountToDecrease, Long accountId) {
        String sql = "update account set balance = balance - ? where account_id = ?";
        try {
            jdbcTemplate.update(sql, amountToDecrease, accountId);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    private Transfer mapRowToTransfer(SqlRowSet set) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(set.getLong("transfer_id"));
        transfer.setTransferTypeId(set.getLong("transfer_type_id"));
        transfer.setTransferStatusId(set.getLong("transfer_status_id"));
        transfer.setAccountFrom(set.getLong("account_from"));
        transfer.setAccountTo(set.getLong("account_to"));
        transfer.setAmount(set.getBigDecimal("amount"));
        return transfer;
    }
}
