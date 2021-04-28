package service;

import exception.ServiceException;
import structures.Customer;

public interface CustomerService {

    boolean applyCustomerAcc(String firstname, String lastname, String username, String password);
    boolean isUsernameAvailable(String username);
    Customer login(String username, String password);
    double viewAccBalance(String bankAccount);
    boolean applyBankAcc(String name, double balance , Customer customer);
    void postTransfer(Customer sender, Customer recipient, double amount, String senderName) throws ServiceException;
    boolean acceptTransfer(Customer recipient, String accountName, String acceptedAcc) throws ServiceException;
    boolean deposit(String name, double amount, Customer customer) throws ServiceException;
    void withdraw(String name, Customer customer, double amount) throws ServiceException;
    Customer getCustomerByUsername(String username);
    void displayCustomerByTransfer(Customer customer);
    boolean checkAccStatus(Customer customer, String account);

}
