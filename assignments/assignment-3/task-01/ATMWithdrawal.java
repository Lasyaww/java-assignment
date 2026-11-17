import java.util.InputMismatchException;
import java.util.Scanner;

/** Q1 - ATM withdrawal with specific exception handling and finally. */
public class ATMWithdrawal {
    static class InsufficientBalanceException extends Exception {
        InsufficientBalanceException(String message) { super(message); }
    }

    static double withdraw(double balance, double amount)
            throws InsufficientBalanceException {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative.");
        if (amount > balance) throw new InsufficientBalanceException("Insufficient balance.");
        return balance - amount;
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            double balance = 5000;
            System.out.print("Enter withdrawal amount: ");
            double amount = input.nextDouble();
            System.out.println("Remaining balance: Rs " + withdraw(balance, amount));
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: enter a numeric amount.");
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid withdrawal: " + e.getMessage());
        } finally {
            System.out.println("ATM transaction attempt completed.");
        }
    }
}
