package dao;

import structures.*;

import java.util.List;

public interface CustomerDAO {
   boolean applyCustomerAcc(String firstname, String lastname, String username, String password, long balance);
   Customer login(String username, String password);
   long viewAccBalance(String bankAccount);
   boolean applyBankAcc(String name, long balance , Customer customer);
   boolean postTransfer();
   List<BankAccount> viewAllAcc(Customer customer);
   boolean acceptTransfer();
   boolean deposit(String name, long amount, Customer customer);
   boolean withdraw();
}
