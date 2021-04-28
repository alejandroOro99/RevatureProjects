package dao;

import db_connection.DBConnection;
import exception.ServiceException;
import org.apache.log4j.Logger;
import structures.BankAccount;
import structures.Customer;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class CustomerDAOImpl implements CustomerDAO{

    private static Logger log = Logger.getLogger(CustomerDAOImpl.class);
    Random randomId = new Random();



        /*
        This method is used every time the user creates a new acc, first a query is executed testing for bankaccid
        and testing to see if a randomly generated one is indeed unique.
         */
        @Override
        public boolean applyCustomerAcc(String firstname, String lastname, String username, String password) {


            boolean idIsUnique = true;
            long randId = 0;
            boolean applySuccess = false;

            try(Connection connection = DBConnection.getConnection()){
                do{
                    String sqlCheckBankAccId = "SELECT bankaccid from \"BankApp\".bankaccounts";
                    PreparedStatement prepareSQL = connection.prepareStatement(sqlCheckBankAccId);
                    ResultSet resultSet = prepareSQL.executeQuery();
                    randId = randomId.nextLong();

                    while(resultSet.next() && idIsUnique){

                        if(resultSet.getLong("bankaccid") == randId){
                            idIsUnique = false;
                            prepareSQL.close();
                        }

                    }

                }while(!idIsUnique);

                //Once we know that randId is unique, insert given name and found randId into a new customer entrance
                String insertSQL = "INSERT INTO \"BankApp\".customers(firstname,lastname,username," +
                        "password,bankaccid) VALUES(?,?,?,?,?)";
                PreparedStatement prepareInsertSQL = connection.prepareStatement(insertSQL);
                prepareInsertSQL.setString(1,firstname);
                prepareInsertSQL.setString(2,lastname);
                prepareInsertSQL.setString(3,username);
                prepareInsertSQL.setString(4,password);
                prepareInsertSQL.setLong(5,randId);

                applySuccess = prepareInsertSQL.execute();
                log.debug("Applied successfully under the name: "+firstname+" "+lastname+",username: "+username +
                        " password: "+password+" unique BankAccId: "+randId);

            }catch(SQLException e){

                log.debug(e);

            }

           return applySuccess;
        }

        //Method that can check if an user's credentials match an entry in the database
    @Override
    public Customer login(String username, String password) {

            try(Connection connection = DBConnection.getConnection()){

                //the sql query, then the statement then the execution of the query
                String sql = "SELECT username, password, bankaccid from \"BankApp\".customers";
                PreparedStatement queryAccSQL = connection.prepareStatement(sql);
                ResultSet resultSet = queryAccSQL.executeQuery();

                while(resultSet.next()){

                    boolean isUsernameCorrect = resultSet.getString("username").equals(username) ;
                    boolean isPasswordCorrect = resultSet.getString("password").equals(password);

                    //returns a customer object with just a bankaccid
                    if(isUsernameCorrect && isPasswordCorrect){
                        log.debug("Successfully login in");

                        return new Customer(resultSet.getLong("bankaccid"));
                    }

                }

            }catch(SQLException e){
                log.debug(e);
            }
        log.debug("username or password incorrect");
        return null;
    }

    @Override
    public double viewAccBalance(String name) {

        try(Connection connection = DBConnection.getConnection()){

            String sql = "SELECT balance from \"BankApp\".bankaccounts WHERE name=?";
            PreparedStatement queryAccSQL = connection.prepareStatement(sql);
            queryAccSQL.setString(1,name);
            ResultSet resultSet = queryAccSQL.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble("balance");
            }

        }catch(SQLException e){
            log.debug(e);
        }

        return -1;
    }

    @Override
    public boolean applyBankAcc(String name, double balance, Customer customer) {

            try(Connection connection = DBConnection.getConnection()){

                String sql = "INSERT INTO \"BankApp\".bankaccounts(name,balance,bankaccid,status) VALUES(?,?,?,?)";
                PreparedStatement insertSQL = connection.prepareStatement(sql);
                insertSQL.setString(1,name);
                insertSQL.setDouble(2,balance);
                insertSQL.setLong(3,customer.getBankAccId());
                insertSQL.setInt(4,0);//0, default value for status for accounts pending approval

                boolean resultBool = insertSQL.execute();

                if(!resultBool){
                    return true;
                }

            }catch(SQLException e){
                log.debug(e);
            }

        return false;
    }

    @Override
    public void postTransfer(Customer sender, Customer recipient, double amount, String senderAccName){

            try(Connection connection = DBConnection.getConnection()){
                String sql = "INSERT INTO \"BankApp\".transfers(recipientid, " +
                        "senderid, balance,senderaccname) VALUES(?,?,?,?)";
                PreparedStatement transferSQL = connection.prepareStatement(sql);
                transferSQL.setLong(1,recipient.getBankAccId());
                transferSQL.setLong(2,sender.getBankAccId());//-1 for posted transfers, not real accounts
                transferSQL.setDouble(3,amount);
                transferSQL.setString(4,senderAccName);
                log.debug(transferSQL.executeUpdate());

            }catch(SQLException e){
                log.debug(e);
            }



    }


    @Override
    public boolean acceptTransfer(Customer recipient, String accountName, String acceptedAcc) {

            try(Connection connection = DBConnection.getConnection()){

                String sql = "SELECT balance, senderid FROM " +
                        "\"BankApp\".transfers WHERE recipientid=? AND senderaccname=?";
                PreparedStatement acceptTransferSQL = connection.prepareStatement(sql);
                acceptTransferSQL.setLong(1,recipient.getBankAccId());
                acceptTransferSQL.setString(2,acceptedAcc);
                ResultSet resultSet = acceptTransferSQL.executeQuery();

                while(resultSet.next()){

                    double balanceDiscount = resultSet.getDouble("balance");
                    long senderId = resultSet.getLong("senderid");

                    String sqlSenderDiscount = "UPDATE \"BankApp\".bankaccounts SET balance=balance-? WHERE bankaccid=?";
                    PreparedStatement senderDiscountSQL = connection.prepareStatement(sqlSenderDiscount);
                    senderDiscountSQL.setDouble(1,balanceDiscount);
                    senderDiscountSQL.setLong(2,senderId);
                    senderDiscountSQL.executeUpdate();

                    String sqlRecipientAdd = "UPDATE \"BankApp\".bankaccounts SET balance=balance+? WHERE bankaccid=?";
                    PreparedStatement recipientAddSQL = connection.prepareStatement(sqlRecipientAdd);
                    recipientAddSQL.setDouble(1,balanceDiscount);
                    recipientAddSQL.setLong(2,recipient.getBankAccId());
                    recipientAddSQL.executeUpdate();

                    String sqlDeleteTransfer = "DELETE FROM\"BankApp\".transfers WHERE senderid=? AND senderaccname=?";
                    PreparedStatement deleteSQL = connection.prepareStatement(sqlDeleteTransfer);
                    deleteSQL.setLong(1,senderId);
                    deleteSQL.setString(2,acceptedAcc);
                    deleteSQL.executeUpdate();

                }



            }catch(SQLException e){
                log.debug(e);
            }

        return false;
    }

    @Override
    public boolean deposit(String name, double amount, Customer customer) {

            boolean result = true;
            try(Connection connection = DBConnection.getConnection()){
                String sql = "UPDATE \"BankApp\".bankaccounts SET balance=balance + ? WHERE name=? AND bankaccid = ?";
                PreparedStatement updateSQL = connection.prepareStatement(sql);
                updateSQL.setDouble(1,amount);
                updateSQL.setString(2,name);
                updateSQL.setLong(3,customer.getBankAccId());
                result = updateSQL.execute();

            }catch(SQLException e){
                log.debug(e);
            }

        return !result;
    }

    //UPDATES sql account given customer bankaccid and name of account as well as the amount to withdraw2
    @Override
    public void withdraw(String name, Customer customer, double amount) throws ServiceException {

            try(Connection connection = DBConnection.getConnection()){

                String sqlCheck = "SELECT balance from \"BankApp\".bankaccounts WHERE bankaccid=? AND name=?";
                PreparedStatement checkSQL = connection.prepareStatement(sqlCheck);
                checkSQL.setLong(1,customer.getBankAccId());
                checkSQL.setString(2,name);
                ResultSet resultSetCheck = checkSQL.executeQuery();

                resultSetCheck.next();
                if(resultSetCheck.getDouble("balance") < amount){
                    throw new ServiceException("No withdraw amounts that result in negative balances");
                }

                String sql = "UPDATE \"BankApp\".bankaccounts SET balance = balance- ? WHERE name=? AND bankaccid=?";
                PreparedStatement withdrawSQL = connection.prepareStatement(sql);
                withdrawSQL.setDouble(1,amount);
                withdrawSQL.setString(2,name);
                withdrawSQL.setLong(3,customer.getBankAccId());
                int resultNum = withdrawSQL.executeUpdate();

                if(resultNum == 1){
                    log.debug("Withdrew $"+amount+" successfully");
                }else{
                    log.debug("Could not withdraw from account");
                }
            }catch(SQLException e){
                log.debug(e);
            }

    }

    @Override
    public Customer getCustomerByUsername(String username) {

            try(Connection connection = DBConnection.getConnection()){

                String sql = "SELECT bankaccid FROM \"BankApp\".customers WHERE username =?";
                PreparedStatement getCustomerSQL = connection.prepareStatement(sql);
                getCustomerSQL.setString(1,username);
                ResultSet resultSet = getCustomerSQL.executeQuery();

                if(resultSet.next()){
                    return new Customer(resultSet.getLong("bankaccid"));
                }


            }catch(SQLException e){
                log.debug(e);
            }
        return null;
    }

    @Override
    public void displayCustomerByTransfer(Customer customer) {

            try(Connection connection = DBConnection.getConnection()){
                String sql = "SELECT senderaccname,balance FROM \"BankApp\".transfers WHERE recipientid=?";
                PreparedStatement displaySQL = connection.prepareStatement(sql);
                displaySQL.setLong(1,customer.getBankAccId());
                ResultSet resultSet = displaySQL.executeQuery();

                while(resultSet.next()){
                    log.debug(resultSet.getString("senderaccname")+
                            " amount: $"+resultSet.getDouble("balance"));
                }

            }catch(SQLException e){
                log.debug(e);
            }
    }


    @Override
    public boolean checkAccStatus(Customer customer, String account) {

            try(Connection connection = DBConnection.getConnection()){

                String sql = "SELECT status From \"BankApp\".bankaccounts WHERE bankaccid=? AND name=?";
                PreparedStatement selectStatusSQL = connection.prepareStatement(sql);
                selectStatusSQL.setLong(1,customer.getBankAccId());
                selectStatusSQL.setString(2,account);
                ResultSet resultSet = selectStatusSQL.executeQuery();

                if(resultSet.next()){
                    if(resultSet.getInt("status") == 0){
                        return false;
                    }else{
                        return true;
                    }

                }else{
                    return false;
                }

            }catch(SQLException e){
                log.debug(e);
            }
        return false;
    }

    @Override
    public boolean isUsernameAvailable(String username) {

            try(Connection connection = DBConnection.getConnection()){
                String sql = "SELECT bankaccid FROM \"BankApp\".customers WHERE username=?";
                PreparedStatement checkUsernameSQL = connection.prepareStatement(sql);
                checkUsernameSQL.setString(1,username);

                ResultSet resultSet = checkUsernameSQL.executeQuery();

                if(resultSet.next()){
                    log.debug("false");
                    return false;
                }else{
                    log.debug("true");
                    return true;
                }


            }catch(SQLException e){
                log.debug(e);
            }
        return false;
    }

}
