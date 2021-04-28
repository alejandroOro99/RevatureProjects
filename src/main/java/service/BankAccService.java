package service;

import structures.BankAccount;
import structures.Customer;

import java.util.List;

public interface BankAccService{
    List<BankAccount> viewAccounts(Customer customer);
    Customer selectCustomer(String username, String password);
    void getStatusZeroAccounts(Customer customer);
    void acceptAccounts(String name, Customer customer);
}
