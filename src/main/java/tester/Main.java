package tester;

import java.util.List;
import java.util.Scanner;

import dao.*;
import org.apache.log4j.Logger;

import service.*;
import structures.BankAccount;
import structures.Customer;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean endSession = false;
        do{
            log.debug("Press 1 to apply for a customer account");
            log.debug("Press 2 to login as a customer");
            log.debug("Press 3 to login as employee");
            log.debug("Press 0 to exit the application");
            int response = Integer.parseInt(scanner.nextLine());

            switch(response){
                case 1:
                    log.debug("Enter your name");
                    String firstName = scanner.nextLine();

                    log.debug("Enter your last name");
                    String lastname = scanner.nextLine();

                    log.debug("Enter desired username");
                    String usernameApply = scanner.nextLine();

                    log.debug("Enter your password");
                    String passwordApply = scanner.nextLine();

                    log.debug("Enter your starting balance");
                    long balanceApply = Long.parseLong(scanner.nextLine());

                    CustomerService customerApply = new CustomerServiceImpl();
                    customerApply.applyCustomerAcc(firstName,lastname,usernameApply,passwordApply, balanceApply);

                    break;
                case 2:
                    log.debug("Enter your username");
                    String username = scanner.nextLine();
                    log.debug("Enter your password");
                    String password = scanner.nextLine();
                    CustomerDAO customerDAO = new CustomerDAOImpl();
                    Customer customerApplyBankAcc = customerDAO.login(username, password);

                    //new customer menu
                    log.debug("Login success");
                    log.debug("Press 1 to apply for a bank account");
                    log.debug("Press 2 to deposit to one of your accounts");
                    int customerMenu = Integer.parseInt(scanner.nextLine());

                    switch(customerMenu) {

                        case 1:
                            log.debug("Enter the account's name");
                            String accName = scanner.nextLine();
                            log.debug("Enter the starting balance");
                            long accBalance = Long.parseLong(scanner.nextLine());

                            log.debug(customerDAO.applyBankAcc(accName, accBalance, customerApplyBankAcc));

                            break;
                        case 2:
                            log.debug("Enter the account's name");
                            String accDeposit = scanner.nextLine();
                            log.debug("Enter the amount to deposit");
                            long depositAmount = Long.parseLong(scanner.nextLine());

                            log.debug(customerDAO.deposit(accDeposit, depositAmount, customerApplyBankAcc));

                            break;

                    }

                    break;
                case 3:
                    log.debug("Press 1 to view accounts of given customer");
                    int resultCase3 = Integer.parseInt(scanner.nextLine());

                        switch(resultCase3){
                            case 1:
                                BankAccDAO bankAccDAOC1C3 = new BankAccDAOImpl();
                                log.debug("Put username of user");
                                String usernameViewAcc = scanner.nextLine();
                                log.debug("Put password of user");
                                String passwordViewAcc = scanner.nextLine();

                                Customer customerViewAcc = bankAccDAOC1C3.selectCustomer(usernameViewAcc,passwordViewAcc);
                                List<BankAccount> bankAccountListViewAcc = bankAccDAOC1C3.viewAccounts(customerViewAcc);
                                log.debug(bankAccountListViewAcc);

                                break;
                            default:
                                log.debug("Wrong input");
                                break;
                        }
                    break;
                case 0:
                    endSession = true;
                    break;
                default:
                    log.debug("Wrong input");
                    break;
            }

        }while(!endSession);



    }

}
