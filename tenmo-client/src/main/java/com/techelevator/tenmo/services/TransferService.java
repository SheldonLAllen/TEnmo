package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {

    private final String baseAPI;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseAPI) {
        this.baseAPI = baseAPI;
    }

    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = null;
        try {
            balance = restTemplate.getForObject(baseAPI + user.getUser().getId(), BigDecimal.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("TransferService.getBalance catch block");
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    public Transfer[] getTransfers() {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.getForObject(baseAPI + "/transfers", Transfer[].class);
        } catch (ResourceAccessException | RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public boolean sendMoney(BigDecimal amount, AuthenticatedUser user, Long IdToEndUser) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(1L);
        transfer.setTransferStatusId(1L);
        transfer.setAccountFrom(restTemplate.getForObject(baseAPI + "user-account/" + user.getUser().getId(), Long.class));
        transfer.setAccountTo(restTemplate.getForObject(baseAPI + "user-account/" + IdToEndUser, Long.class));
        transfer.setAmount(amount);
        HttpEntity<Transfer> entity = makeTransferEntity(transfer, user);
        try {
            restTemplate.postForObject(baseAPI + "new-transfer", entity, Boolean.class);
            restTemplate.put(baseAPI + "decrease-balance", entity, Boolean.class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
        return true;
    }

    private HttpEntity<String> makeEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(user.getUser().getUsername(), headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
