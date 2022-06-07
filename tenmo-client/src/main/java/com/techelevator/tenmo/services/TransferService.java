package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    private final String baseAPI;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseAPI) {
        this.baseAPI = baseAPI;
    }

//    public BigDecimal getBalance(AuthenticatedUser user) {
//        HttpEntity<String> entity = makeEntity(user);
//        BigDecimal balance = null;
//        try {
//            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseAPI + "/balance", HttpMethod.GET, entity, BigDecimal.class);
//            balance = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            System.out.println("TransferService.getBalance catch block");
//            BasicLogger.log(e.getMessage());
//        }
//        return balance;
//    }

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

    private HttpEntity<String> makeEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(user.getUser().getUsername(), headers);
    }
}
