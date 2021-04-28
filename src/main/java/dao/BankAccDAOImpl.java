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

    @Override
    public void getStatusZeroAccounts(Customer customer) {

        try(Connection connection = DBConnection.getConnection()){

            String sql = "SELECT name FROM \"BankApp\".bankaccounts WHERE status = 0 AND bankaccid=?";
            PreparedStatement acceptAccSQL = connection.prepareStatement(sql);
            acceptAccSQL.setLong(1,customer.getBankAccId());
            ResultSet resultSet = acceptAccSQL.executeQuery();

            while(resultSet.next()){
                log.debug(resultSet.getString("name"));
            }

        }catch(Exception e){
            log.debug(e);
        }
    }

    @Override
    public void acceptAccounts(String name, Customer customer) {

        String[] accNames = name.split(" ");//split based on spaces
        try(Connection connection = DBConnection.getConnection()){
            String sql = "UPDATE \"BankApp\".bankaccounts SET status = 1 WHERE name=? AND bankaccid=?";
            String sqlDelete = "DELETE FROM \"BankApp\".bankaccounts WHERE status = 0 AND bankaccid = ?";

            PreparedStatement acceptAccountsSQL = connection.prepareStatement(sql);

            for(String namesOfAccs : accNames){
                acceptAccountsSQL.setString(1,namesOfAccs);
                acceptAccountsSQL.setLong(2,customer.getBankAccId());

                acceptAccountsSQL.executeUpdate();

            }

            PreparedStatement deleteAccSQL = connection.prepareStatement(sqlDelete);
            deleteAccSQL.setLong(1,customer.getBankAccId());
            deleteAccSQL.executeUpdate();


        }catch(SQLException e){
            log.debug(e);
        }

    }
}
