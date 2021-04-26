package dao;

import db_connection.DBConnection;
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
        public boolean applyCustomerAcc(String firstname, String lastname, String username, String password, long balance) {


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
    public long viewAccBalance(String name) {

        try(Connection connection = DBConnection.getConnection()){

            String sql = "SELECT balance from \"BankApp\".customers WHERE name="+name;
            PreparedStatement queryAccSQL = connection.prepareStatement(sql);
            ResultSet resultSet = queryAccSQL.executeQuery();

            while(resultSet.next()){



            }

        }catch(SQLException e){
            log.debug(e);
        }

        return 0;
    }

    @Override
    public boolean applyBankAcc(String name, long balance, Customer customer) {

            try(Connection connection = DBConnection.getConnection()){

                String sql = "INSERT INTO \"BankApp\".bankaccounts(name,balance,bankaccid,status) VALUES(?,?,?,?)";
                PreparedStatement insertSQL = connection.prepareStatement(sql);
                insertSQL.setString(1,name);
                insertSQL.setLong(2,balance);
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
    public boolean postTransfer() {
        return false;
    }

    @Override
    public List<BankAccount> viewAllAcc(Customer customer) {
        return null;
    }

    @Override
    public boolean acceptTransfer() {
        return false;
    }

    @Override
    public boolean deposit(String name, long amount, Customer customer) {

            boolean result = true;
            try(Connection connection = DBConnection.getConnection()){
                String sql = "UPDATE \"BankApp\".bankaccounts SET balance=balance + ? WHERE name=? AND bankaccid = ?";
                PreparedStatement updateSQL = connection.prepareStatement(sql);
                updateSQL.setLong(1,amount);
                updateSQL.setString(2,name);
                updateSQL.setLong(3,customer.getBankAccId());
                result = updateSQL.execute();

            }catch(SQLException e){
                log.debug(e);
            }

        return !result;
    }

    @Override
    public boolean withdraw() {
        return false;
    }

}
