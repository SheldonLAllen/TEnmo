package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO NumberFormatter for currency
        NumberFormat format = NumberFormat.getCurrencyInstance();
        BigDecimal currentBalance = transferService.getBalance(currentUser);
        format.format(currentBalance);
        System.out.println(currentBalance);
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        consoleService.printTransfers(transferService.getTransfers());
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        consoleService.printUsers();
        Long id = checkIDIsNotUsers(1);
        if (id == 0) return;

        BigDecimal moneyToTransfer = consoleService.promptForBigDecimal("Enter amount: ");

        if (moneyToTransfer.compareTo(transferService.getBalance(currentUser)) <= 0 && moneyToTransfer.intValue() > 0) {
            System.out.println("you can send money");
            transferService.sendMoney(moneyToTransfer, currentUser, id);
        }else {
            System.out.println("The amount you are requesting to send is either more than you have, or less than or equal to $0");
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        consoleService.printUsers();
	}

    private Long checkIDIsNotUsers(int transferType) {
        String first = "";
        String second = "";
        Long idChosen;
        if (transferType == 1) {
            first = "Enter ID of user you are sending to (0 to cancel): ";
            second = "You cannot send money to yourself.";
        }else {
            first = "Enter ID of user you are requesting from (0 to cancel): ";
            second = "You cannot request money from yourself.";
        }
        while(true) {
            idChosen = consoleService.promptForLong(first);
            if (idChosen == 0) {
                break;
            } else if (idChosen.equals(currentUser.getUser().getId())) {
                System.out.println(second);
                continue;
            } else if (!consoleService.userExists(idChosen)) {
                System.out.println("There is no user with that ID.");
                continue;
            }
            break;
        }
        return idChosen;
    }

}
