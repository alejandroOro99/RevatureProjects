package dao;

import db_connection.DBConnection;
import org.apache.log4j.Logger;
import structures.BankAccount;
import structures.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccDAOImpl implements BankAccDAO{

    private static Logger log = Logger.getLogger(BankAccDAOImpl.class);


    @Override
    public List<BankAccount> viewAccounts(Customer customer) {

        List<BankAccount> bankAccountList = new ArrayList<>();

        try(Connection connection = DBConnection.getConnection()){

            String sql = "SELECT bankaccid,balance FROM \"BankApp\".bankaccounts WHERE bankaccid = ?";
            PreparedStatement selectSQL = connection.prepareStatement(sql);
            selectSQL.setLong(1,customer.getBankAccId());

            ResultSet resultSet = selectSQL.executeQuery();

            while(resultSet.next()){
                BankAccount bankAccount = new BankAccount(customer.getBankAccId(), resultSet.getLong("balance"));
                bankAccountList.add(bankAccount);
            }


        }catch(SQLException e){
            log.debug(e);
        }

        return bankAccountList;
    }

    @Override
    public Customer selectCustomer(String username, String password) {

        try(Connection connection = DBConnection.getConnection()){

            String sql = "SELECT username, password, bankaccid from \"BankApp\".customers";
            PreparedStatement queryAccSQL = connection.prepareStatement(sql);
            ResultSet resultSet = queryAccSQL.executeQuery();

            while(resultSet.next()){

                boolean isUsernameCorrect = resultSet.getString("username").equals(username) ;
                boolean isPasswordCorrect = resultSet.getString("password").equals(password);

                if(isUsernameCorrect && isPasswordCorrect){
                    log.debug("Successfully found account");
                    Customer customer = new Customer(resultSet.getLong("bankaccid"));
                    return customer;
                }

            }

        }catch(SQLException e){
            log.debug(e);
        }

        return null;
    }
}
