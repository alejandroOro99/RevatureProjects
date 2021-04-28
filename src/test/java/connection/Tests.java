package connection;

import dao.BankAccDAO;
import dao.BankAccDAOImpl;
import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.CustomerService;
import service.CustomerServiceImpl;
import structures.Customer;

public class Tests {
    private static CustomerDAO customerDAO;
    private static BankAccDAO bankAccDAO;
    private static CustomerService customerService = new CustomerServiceImpl();

    @BeforeAll
    public static void setup(){
        customerDAO = new CustomerDAOImpl();
        bankAccDAO = new BankAccDAOImpl();
    }

    @Test
    public void testSelectCustomerFalse(){
        String username = "";
        Assertions.assertEquals(null,customerDAO.getCustomerByUsername(username));
    }
    @Test
    public void testCheckAccStatus(){
        Customer customer = new Customer(5775280242015442471l);
        String account = "orozcoAcc";
        Assertions.assertEquals(true,customerDAO.checkAccStatus(customer, account));
    }
    @Test
    public void testCheckAccStatusFalse(){
        Customer customer = new Customer(5775280242015442471l);
        String account = "yu";
        Assertions.assertEquals(false,customerDAO.checkAccStatus(customer, account));
    }
    @Test
    public void testIsUsernameAvailable(){
        String username ="";
        Assertions.assertEquals(true, customerDAO.isUsernameAvailable(username));
    }
    @Test
    public void testIsUsernameAvailableFalse(){
        String username = "1234";
        Assertions.assertEquals(false, customerDAO.isUsernameAvailable(username));
    }
    @Test
    public void testDeposit1(){
        Customer customer = new Customer(5775280242015442471l);
        String name = "orozcoAcc";
        double amount = 10;
        Assertions.assertEquals(true,customerDAO.deposit(name, amount, customer));
    }
    @Test
    public void testDeposit2() throws ServiceException {
        Exception e = new Exception();
        Customer customer = new Customer(5775280242015442471l);
        String name ="";
        double amount = 10;
        Assertions.assertEquals(false,customerService.deposit(name, amount, customer));
    }
    @Test
    public void testDeposit3(){
        Customer customer = new Customer(5775280242015442471l);
        String name = "orozcoAcc";
        double amount = -1;

        Assertions.assertEquals(true,customerDAO.deposit(name, amount, customer));
    }
    @Test
    public void testAcceptTransfer1(){
        Customer customer = new Customer(-1033555330049077587l);
        String accepted = "orozcoAcc";
        String accName = "yu";
        Assertions.assertEquals(false,customerDAO.acceptTransfer(customer, accName, accepted));
    }
    @Test
    public void testAcceptTransfer2(){
        Customer customer = new Customer(-1033555330049077587l);
        String accepted = "yu";
        String accName = "orozcoAcc";
        Assertions.assertEquals(false,customerDAO.acceptTransfer(customer, accName, accepted));
    }

}
