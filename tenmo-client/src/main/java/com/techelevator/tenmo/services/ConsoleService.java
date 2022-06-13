package com.techelevator.tenmo.services;


import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final String baseAPI;
    private RestTemplate restTemplate = new RestTemplate();

    public ConsoleService(String baseAPI) {
        this.baseAPI = baseAPI;
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public Long promptForLong(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void printUsers() {
        User[] users = null;
        try {
            users = restTemplate.getForObject(baseAPI + "/users", User[].class);
            if (users == null) {
                System.out.println("There are no users to send TE Bucks to.");
            }
            System.out.println("-----------------------------------------");
            System.out.println("Users");
            System.out.println("ID          Name");
            System.out.println("-----------------------------------------");
            for (User user : users) {
                System.out.println(user.getId() + "        " + user.getUsername());
            }
            System.out.println("-----------------------------------------");
        } catch (ResourceAccessException | RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void printTransfers(Transfer[] transfers) {
        System.out.println("-----------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID      From     To         Amount");
        System.out.println("-----------------------------------------");
        Long id;
        String from;
        String to;
        BigDecimal balance;
        if (transfers == null) {
            System.out.println("There are no transfers to display");
        }else {
            for (Transfer transfer : transfers) {
                id = transfer.getTransferId();
                from = restTemplate.getForObject(baseAPI + "/users/" + transfer.getAccountFrom(), String.class);
                to = restTemplate.getForObject(baseAPI + "/users/" + transfer.getAccountTo(), String.class);
                balance = transfer.getAmount();
                System.out.println(id + "   " + from + "   " + to + "   " + balance.toString());
            }
            System.out.println("-----------------------------------------");
        }
    }

    public void printTransfer(Long id) {
        System.out.println("-----------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-----------------------------------------");
        Transfer transfer = null;
        try {
            transfer = restTemplate.getForObject(baseAPI + "/transfer/" + id, Transfer.class);
        } catch (ResourceAccessException | RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }

        if (transfer == null) {
            System.out.println("No transfer exists for the given id:" + id);
        } else {
            printSingleTransfer(transfer);
        }
    }

    public void printSingleTransfer(Transfer transfer) {
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getAccountFrom());
        System.out.println("To: " + transfer.getAccountTo());
        System.out.println("Type: " + transfer.getTransferTypeId());
        System.out.println("Status: " + transfer.getTransferStatusId());
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public boolean userExists(Long id) {
        User[] users = null;
        try {
            users = restTemplate.getForObject(baseAPI + "/users", User[].class);
            if (users != null) {
                for (User user : users) {
                    if (id.equals(user.getId())) return true;
                }
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return false;
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
