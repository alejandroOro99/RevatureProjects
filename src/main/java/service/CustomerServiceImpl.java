package service;

import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import exception.ServiceException;
import org.apache.log4j.Logger;
import structures.Customer;

public class CustomerServiceImpl implements CustomerService {
    private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
    private CustomerDAO customerDAO= new CustomerDAOImpl();

    @Override
    public boolean applyCustomerAcc(String firstname, String lastname, String username, String password) {


            return customerDAO.applyCustomerAcc(firstname, lastname, username, password);


    }

    @Override
    public boolean isUsernameAvailable(String username) {

        if(customerDAO.isUsernameAvailable(username)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Customer login(String username, String password) {


        return customerDAO.login(username,password);
    }

    @Override
    public double viewAccBalance(String bankAccount) {

        return customerDAO.viewAccBalance(bankAccount);
    }

    @Override
    public boolean applyBankAcc(String name, double balance, Customer customer) {

        if(balance < 0){
            throw new SecurityException("cant have a negative balance for an account");
        }else{
            return customerDAO.applyBankAcc(name, balance, customer);
        }

    }

    @Override
    public void postTransfer(Customer sender, Customer recipient, double amount, String senderName) throws ServiceException {

        if(!customerDAO.checkAccStatus(sender,senderName)){
            throw new ServiceException("Account must exist and be accepted by a banker");
        }else if(customerDAO.viewAccBalance(senderName) < amount){
            throw new ServiceException("Can't post a transfer that results in a negative balance");
        }else{
            customerDAO.postTransfer(sender, recipient, amount, senderName);
        }

    }

    @Override
    public boolean acceptTransfer(Customer recipient, String accountName, String acceptedAcc) throws ServiceException {

        if(!customerDAO.checkAccStatus(recipient, accountName)){
            throw new ServiceException("Account must exist and be approved by a banker");
        }else{
            return customerDAO.acceptTransfer(recipient, accountName, acceptedAcc);
        }

    }

    @Override
    public boolean deposit(String name, double amount, Customer customer) throws ServiceException {
        if(amount < 0){
            throw new ServiceException("No negative deposit amounts");
        }else if(!customerDAO.checkAccStatus(customer, name)){
            throw new ServiceException("Account must exist and have been accepted by a banker to be used");
        }else{
            return customerDAO.deposit(name, amount, customer);
        }

    }

    @Override
    public void withdraw(String name, Customer customer, double amount) throws ServiceException {

        if(amount < 0){
            throw new ServiceException("No negative withdraw values");
        }else if(!customerDAO.checkAccStatus(customer, name)){
            throw new ServiceException("Account must exist and have been accepted by a banker to be used");
        }else{
            try{
                customerDAO.withdraw(name, customer, amount);
            }catch(ServiceException e){
                log.debug(e);
            }

        }

    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerDAO.getCustomerByUsername(username);
    }

    @Override
    public void displayCustomerByTransfer(Customer customer) {

        customerDAO.displayCustomerByTransfer(customer);
    }

    @Override
    public boolean checkAccStatus(Customer customer, String account) {


        return customerDAO.checkAccStatus(customer, account);
    }
}
