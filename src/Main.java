import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class Transaction {
    private final String type;
    private final double amount;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f at %s",
                timestamp.toLocalTime().withNano(0),
                type,
                amount,
                timestamp.toLocalDate());
    }
}

abstract class Account {
    private final String accountNumber;
    private double balance;
    private final List<Transaction> history;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.history = new ArrayList<>();
        System.out.println("New Account " + accountNumber + " created with balance: $" + initialBalance);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            history.add(new Transaction("Deposit", amount));
            System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            history.add(new Transaction("Withdrawal", amount));
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
        } else if (amount > balance) {
            System.out.println("Withdrawal failed: Insufficient funds.");
        } else {
            System.out.println("Withdrawal amount must be positive.");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void printTransactionHistory() {
        System.out.println("\n--- Transaction History for Account " + accountNumber + " ---");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
        System.out.println("------------------------------------------");
    }

    public abstract void applyMonthlyInterest();
}

class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.01;

    public SavingsAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = getBalance() * INTEREST_RATE;
        deposit(interest);
        System.out.printf("Interest of $%.2f applied to Savings Account %s.%n",
                interest, getAccountNumber());
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 500) {
            System.out.println("Withdrawal failed: Savings accounts have a maximum single withdrawal limit of $500.");
            return;
        }
        super.withdraw(amount);
    }
}

public class Main{
    public static void main(String[] args) {
        System.out.println("--- Bank Account Simulation Started ---");

        SavingsAccount mySavings = new SavingsAccount("SA-1001", 500.00);

        mySavings.deposit(150.50);
        mySavings.withdraw(50.00);
        mySavings.withdraw(600.00);
        mySavings.deposit(200.00);

        mySavings.applyMonthlyInterest();

        System.out.printf("\nFinal Balance for %s: $%.2f%n",
                mySavings.getAccountNumber(), mySavings.getBalance());

        mySavings.printTransactionHistory();

        System.out.println("--- Simulation Complete ---");
    }
}