package service;

import dao.BankAccDAO;
import dao.BankAccDAOImpl;
import structures.BankAccount;
import structures.Customer;

import java.util.List;

public class BankAccServiceImpl implements BankAccService{
    private static BankAccDAO bankAccDAO = new BankAccDAOImpl();
    @Override
    public List<BankAccount> viewAccounts(Customer customer) {
        return bankAccDAO.viewAccounts(customer);
    }

    @Override
    public Customer selectCustomer(String username, String password) {

        return bankAccDAO.selectCustomer(username, password);
    }

    @Override
    public void getStatusZeroAccounts(Customer customer) {
        bankAccDAO.getStatusZeroAccounts(customer);
    }

    @Override
    public void acceptAccounts(String name, Customer customer) {
        bankAccDAO.acceptAccounts(name, customer);
    }
}
