package com.test.www;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

class Transaction {
    private String type;       // INCOME or EXPENSE
    private LocalDate date;
    private String category;
    private double amount;

    public Transaction(String type, LocalDate date, String category, double amount) {
        this.type = type;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String toString() {
        return type + "," + date + "," + category + "," + amount;
    }
}

public class ExpenseTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load Transactions from File");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    addTransaction("INCOME");
                    break;
                case 2:
                    addTransaction("EXPENSE");
                    break;
                case 3:
                    viewMonthlySummary();
                    break;
                case 4:
                    loadFromFile();
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTransaction(String type) {
        try {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.print("Enter Category (" +
                    (type.equals("INCOME") ? "Salary/Business" : "Food/Rent/Travel") + "): ");
            String category = scanner.nextLine();

            System.out.print("Enter Amount: ");
            double amount = readDouble();

            Transaction transaction = new Transaction(type, date, category, amount);
            transactions.add(transaction);
            System.out.println(type + " added successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void viewMonthlySummary() {
        try {
            System.out.print("Enter Month and Year (YYYY-MM): ");
            String input = scanner.nextLine();
            String[] parts = input.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            double totalIncome = 0;
            double totalExpense = 0;
            Map<String, Double> incomeMap = new HashMap<>();
            Map<String, Double> expenseMap = new HashMap<>();

            for (Transaction t : transactions) {
                LocalDate date = t.getDate();
                if (date.getYear() == year && date.getMonthValue() == month) {
                    if (t.getType().equals("INCOME")) {
                        totalIncome += t.getAmount();
                        incomeMap.put(t.getCategory(),
                                incomeMap.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                    } else {
                        totalExpense += t.getAmount();
                        expenseMap.put(t.getCategory(),
                                expenseMap.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                    }
                }
            }

            System.out.println("\n--- Summary for " + Month.of(month) + " " + year + " ---");
            System.out.println("Total Income: ₹" + totalIncome);
            for (String cat : incomeMap.keySet()) {
                System.out.println("  " + cat + ": ₹" + incomeMap.get(cat));
            }
            System.out.println("Total Expense: ₹" + totalExpense);
            for (String cat : expenseMap.keySet()) {
                System.out.println("  " + cat + ": ₹" + expenseMap.get(cat));
            }
            System.out.println("Net Savings: ₹" + (totalIncome - totalExpense));
        } catch (Exception e) {
            System.out.println("Invalid input. Try again.");
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter file name (e.g., input.txt): ");
        String fileName = scanner.nextLine();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                String type = parts[0].toUpperCase();
                LocalDate date = LocalDate.parse(parts[1]);
                String category = parts[2];
                double amount = Double.parseDouble(parts[3]);
                transactions.add(new Transaction(type, date, category, amount));
            }
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        } catch (Exception e) {
            System.out.println("File contains invalid data.");
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
