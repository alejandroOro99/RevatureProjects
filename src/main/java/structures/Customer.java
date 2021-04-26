package structures;

import java.util.List;

public class Customer {

    private List<BankAccount> bankAccount;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private long bankAccId;

    public Customer(List<BankAccount> bankAccount, String firstName, String lastName,
                    String username, String password, long bankAccId) {
        this.bankAccount = bankAccount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.bankAccId = bankAccId;
    }

    public Customer(long bankAccId){
        this.bankAccId = bankAccId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "bankAccount=" + bankAccount +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bankAccId=" + bankAccId +
                '}';
    }

    //Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount.add(bankAccount);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(long bankAccId) {
        this.bankAccId = bankAccId;
    }

}
