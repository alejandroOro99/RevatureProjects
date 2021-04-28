package tester;

import java.util.List;
import java.util.Scanner;
import java.io.*;
import dao.*;
import exception.ServiceException;
import org.apache.log4j.Logger;

import service.*;
import structures.BankAccount;
import structures.Customer;

/*
Key features of the bank, customers must have unique usernames as well as unique bank account names within the
respective user account.
i.e: I can have an user whose username is Michael and has a bank account named michaelAcc
and I can also have a John with a bank account name michaelAcc, but i can't have two michaelAcc in John's acc.


Bank accounts and user accounts are linked by a random and unique bankaccid, but this id is unique to users
almost like a Social security number and this number is used to tie bank accounts to the unique user.
Therefore a person may open many accounts but is required to have only one user account.

 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);
    private static Scanner scanner = new Scanner(System.in);
    private static final Logger transactions = Logger.getLogger("transactionsLogger");
    private static File file = new File(
            "C:\\Users\\aleja\\IdeaProjects\\Project_Zero\\transactions.log");
    private static Scanner fileScanner;

    static {
        try {
            fileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            log.debug(e);
        }
    }


    private static void customerMenu(){
        log.debug("Enter your username");
        String username = scanner.nextLine();
        log.debug("Enter your password");
        String password = scanner.nextLine();
        CustomerService customerService = new CustomerServiceImpl();

        //CUSTOMER LOGGED IN SUCCESS MENU

        int customerMenu;
        boolean customerMenuFlag;
        Customer customerApplyBankAcc = customerService.login(username, password);
        if( customerApplyBankAcc!= null){
            customerMenuFlag = true;
        }else{
            customerMenuFlag = false;
        }


        while(customerMenuFlag){
            transactions.info("User: "+username+" logged in");
            log.debug("Press 1 to apply for a bank account");
            log.debug("Press 2 to deposit to one of your accounts");
            log.debug("Press 3 to view a particular account's balance");
            log.debug("Press 4 to withdraw from an account");
            log.debug("Press 5 to post a transfer to an account");
            log.debug("Press 6 to handle pending transfer requests");
            log.debug("Press 0 to log out");
            customerMenu = Integer.parseInt(scanner.nextLine());
            switch(customerMenu) {
                case 1:
                    try{
                        log.debug("Enter the account's name");
                        String accName = scanner.nextLine();
                        log.debug("Enter the starting balance");
                        long accBalance = Long.parseLong(scanner.nextLine());

                        log.debug(customerService.applyBankAcc(accName, accBalance, customerApplyBankAcc));
                        transactions.info("Bank account created, name: "+accName+" balance: $"+accBalance);
                    }catch(Exception e){
                        log.debug(e);
                    }

                    break;
                case 2:

                    try{

                        log.debug("Enter the account's name");
                        String accDeposit = scanner.nextLine();
                        log.debug("Enter the amount to deposit");
                        long depositAmount = Long.parseLong(scanner.nextLine());

                        log.debug(customerService.deposit(accDeposit, depositAmount, customerApplyBankAcc));
                        transactions.info("deposit made by "+username+
                                " amount: $"+depositAmount);
                    }catch(Exception e){
                        log.debug(e);
                    }


                    break;

                case 3:

                    try{
                        log.debug("Enter the account name");
                        String viewAccBalanceName = scanner.nextLine();
                        log.debug(customerService.viewAccBalance(viewAccBalanceName));
                    }catch(Exception e){
                        log.debug(e);
                    }

                    break;

                case 4:

                    try{
                        log.debug("Enter the account name");
                        String accWithdrawName = scanner.nextLine();
                        log.debug("Enter amount to withdraw");
                        long accWithdrawAmount = Long.parseLong(scanner.nextLine());

                        customerService.withdraw(accWithdrawName, customerApplyBankAcc, accWithdrawAmount);
                        transactions.info("$"+accWithdrawAmount+" withdrawn from:"+accWithdrawName);
                    }catch(NumberFormatException | ServiceException e){
                        log.debug(e);
                    }


                    break;
                case 5:

                    try{
                        log.debug("Enter username of person you want to transfer to");
                        String postTransferUsername = scanner.nextLine();
                        log.debug("Enter amount to transfer");
                        double postTransferAmount = Double.parseDouble(scanner.nextLine());
                        log.debug("Enter the bank account where you will be transferring from");
                        String postTransferSender = scanner.nextLine(); ;
                        Customer recipient = customerService.getCustomerByUsername(postTransferUsername);

                        customerService.postTransfer(customerApplyBankAcc, recipient,
                                postTransferAmount,postTransferSender);
                        transactions.info("Transfer posted, amount: $"+postTransferUsername+
                                " sender account: "+postTransferSender+
                                " recipient account: "+postTransferUsername);
                    }catch(NumberFormatException | ServiceException e){
                        log.debug(e);
                    }

                    break;
                case 6:
                    try{
                        log.debug("Accounts that have posted a transfer for you are below:");
                        customerService.displayCustomerByTransfer(customerApplyBankAcc);
                        log.debug("Select which transfer to accept");
                        String acceptedAccount = scanner.nextLine();
                        log.debug("Select which of your bank accounts to deposit the money to");
                        String accToDeposit = scanner.nextLine();

                        customerService.acceptTransfer(customerApplyBankAcc,accToDeposit, acceptedAccount);
                        transactions.info("Transfer accepted");
                    }catch(Exception e){
                        log.debug(e);
                    }
                    break;
                case 0:
                    customerMenuFlag = false;
                    break;
                default:
                    log.debug("Wrong input");
            }
        }



    }
    private static void employeeMenu() throws IOException {
        CustomerDAO customerDAOSearch = new CustomerDAOImpl();
        boolean employeeFlag = true;
        do{
            //EMPLOYEE MENU
            log.debug("Press 1 to view accounts of given customer");
            log.debug("Press 2 to handle bank account applications");
            log.debug("Press 3 to view a log of all transactions");
            log.debug("Press 0 to log out");
            int resultCase3 = Integer.parseInt(scanner.nextLine());
            BankAccService bankAccService = new BankAccServiceImpl();
            CustomerDAO employeeDAO = new CustomerDAOImpl();
            switch(resultCase3){
                case 1:
                    BankAccDAO bankAccDAOC1C3 = new BankAccDAOImpl();
                    log.debug("Put username of user");
                    String usernameViewAcc = scanner.nextLine();
                    log.debug("Put password of user");
                    String passwordViewAcc = scanner.nextLine();

                    Customer customerViewAcc = bankAccService.selectCustomer(usernameViewAcc,passwordViewAcc);
                    List<BankAccount> bankAccountListViewAcc = bankAccService.viewAccounts(customerViewAcc);
                    log.debug(bankAccountListViewAcc);

                    break;
                case 2:
                    log.debug("Enter the username of the customer");
                    String acceptAccUsername = scanner.nextLine();
                    Customer acceptAccCustomer = employeeDAO.getCustomerByUsername(acceptAccUsername);

                    log.debug("Below are the accounts pending acceptance");

                    bankAccService.getStatusZeroAccounts(acceptAccCustomer);

                    log.debug("Enter the names(separated by a space) of the bank accounts " +
                            "you would like to approve from those above");
                    String acceptedAccounts = scanner.nextLine();

                    bankAccService.acceptAccounts(acceptedAccounts, acceptAccCustomer);


                    break;
                case 3:
                    fileScanner.useDelimiter("\\Z");
                    log.debug(fileScanner.next());
                    break;
                case 0:
                    employeeFlag = false;
                    break;
                default:
                    log.debug("Wrong input");
                    break;
            }
        }while (employeeFlag);

    }
    public static void main(String[] args) {

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

                    CustomerService customerApply = new CustomerServiceImpl();

                    if(customerApply.isUsernameAvailable(usernameApply))
                        customerApply.applyCustomerAcc(firstName,lastname,usernameApply,passwordApply);

                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    try{
                        employeeMenu();
                    }catch (IOException e){
                        log.debug(e);
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
