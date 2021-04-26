package service;

import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import org.apache.log4j.Logger;

public class CustomerServiceImpl implements CustomerService {
    private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
    private CustomerDAO customerDAO= new CustomerDAOImpl();

    @Override
    public boolean applyCustomerAcc(String firstname, String lastname, String username, String password, long balance) {

        if(balance < 0){
            log.debug("Starting balance must be non-negative");
            return false;
        }else{
            return customerDAO.applyCustomerAcc(firstname, lastname, username, password, balance);
        }

    }
}
