package structures;

public class BankAccount {

    private long bankAccId;
    private long balance;
    private String name;
    private int status;

    public BankAccount(long bankAccId, long balance, String name) {
        this.bankAccId = bankAccId;
        this.balance = balance;
        this.name = name;
    }

    public BankAccount(long bankAccId, long balance) {
        this.bankAccId = bankAccId;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                ", bankAccId=" + bankAccId +
                ", balance=" + balance +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(long bankAccId) {
        this.bankAccId = bankAccId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
