package com.mytracker;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {
    static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactions = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Inner class to hold transaction details
    static class Transaction {
        String type; // INCOME or EXPENSE
        double amount;
        String category;
        LocalDate date;

        public Transaction(String type, double amount, String category, LocalDate date) {
            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        @Override
        public String toString() {
            return type + "," + amount + "," + category + "," + date;
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load from File");
            System.out.println("5. Save to File");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> addTransaction("INCOME");
                case 2 -> addTransaction("EXPENSE");
                case 3 -> viewSummary();
                case 4 -> loadFromFile();
                case 5 -> saveToFile();
                case 6 -> {
                    System.out.println("Thank u");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void addTransaction(String type) {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        String category;
        if (type.equals("INCOME")) {
            System.out.print("Enter category (Salary/Business): ");
        } else {
            System.out.print("Enter category (Food/Rent/Travel): ");
        }
        category = scanner.nextLine();

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, formatter);

        Transaction t = new Transaction(type, amount, category, date);
        transactions.add(t);
        System.out.println("Transaction added successfully.");
    }

    static void viewSummary() {
        Map<Month, Double> incomeMap = new HashMap<>();
        Map<Month, Double> expenseMap = new HashMap<>();

        for (Transaction t : transactions) {
            Month month = t.date.getMonth();
            if (t.type.equals("INCOME")) {
                incomeMap.put(month, incomeMap.getOrDefault(month, 0.0) + t.amount);
            } else {
                expenseMap.put(month, expenseMap.getOrDefault(month, 0.0) + t.amount);
            }
        }

        System.out.println("\n=== Monthly Summary ===");
        for (Month m : Month.values()) {
            double income = incomeMap.getOrDefault(m, 0.0);
            double expense = expenseMap.getOrDefault(m, 0.0);
            if (income > 0 || expense > 0) {
                System.out.printf("%s - Income: %.2f | Expense: %.2f | Balance: %.2f%n",
                        m, income, expense, income - expense);
            }
        }
    }

    static void saveToFile() {
        System.out.print("Enter file name to save: ");
        String fileName = scanner.nextLine();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                writer.println(t.toString());
            }
            System.out.println("Transactions saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        System.out.print("Enter file name to load: ");
        String fileName = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            transactions.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String type = parts[0];
                double amount = Double.parseDouble(parts[1]);
                String category = parts[2];
                LocalDate date = LocalDate.parse(parts[3], formatter);

                transactions.add(new Transaction(type, amount, category, date));
            }
            System.out.println("Transactions loaded from " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
