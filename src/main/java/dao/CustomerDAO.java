package dao;

import exception.ServiceException;
import structures.*;

import java.util.List;

public interface CustomerDAO {
   boolean applyCustomerAcc(String firstname, String lastname, String username, String password);
   Customer login(String username, String password);
   double viewAccBalance(String bankAccount);
   boolean applyBankAcc(String name, double balance , Customer customer);
   void postTransfer(Customer sender, Customer recipient, double amount, String senderName);
   boolean acceptTransfer(Customer recipient, String accountName, String acceptedAccount);
   boolean deposit(String name, double amount, Customer customer);
   void withdraw(String name, Customer customer, double amount) throws ServiceException;
   Customer getCustomerByUsername(String username);
   void displayCustomerByTransfer(Customer customer);
   boolean checkAccStatus(Customer customer, String account);
   boolean isUsernameAvailable(String username);
}
