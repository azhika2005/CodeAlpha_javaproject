save as "StockTradingPlatform.java" 
attach any portfolio file to see the  final output . 

import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String type; // BUY or SELL
    Stock stock;
    int quantity;
    double totalPrice;

    public Transaction(String type, Stock stock, int quantity, double totalPrice) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return type + " | " + stock.symbol + " | Qty: " + quantity + " | Total: $" + totalPrice;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double cashBalance;

    public Portfolio(double initialCash) {
        this.cashBalance = initialCash;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > cashBalance) {
            System.out.println("Insufficient funds!");
            return;
        }
        holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
        cashBalance -= cost;
        history.add(new Transaction("BUY", stock, quantity, cost));
        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.symbol, 0);
        if (owned < quantity) {
            System.out.println("Not enough shares to sell!");
            return;
        }
        double gain = stock.price * quantity;
        holdings.put(stock.symbol, owned - quantity);
        cashBalance += gain;
        history.add(new Transaction("SELL", stock, quantity, gain));
        System.out.println("Sold " + quantity + " shares of " + stock.symbol);
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("---- Portfolio ----");
        System.out.printf("Cash Balance: $%.2f\n", cashBalance);
        double totalValue = cashBalance;
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double currentPrice = market.get(symbol).price;
            double value = currentPrice * qty;
            totalValue += value;
            System.out.printf("%s (%d shares @ $%.2f) = $%.2f\n", symbol, qty, currentPrice, value);
        }
        System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
    }

    public void showHistory() {
        System.out.println("---- Transaction History ----");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(cashBalance);
            for (String symbol : holdings.keySet()) {
                out.println(symbol + " " + holdings.get(symbol));
            }
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (Scanner in = new Scanner(new File(filename))) {
            if (in.hasNextDouble()) cashBalance = in.nextDouble();
            while (in.hasNext()) {
                String symbol = in.next();
                int qty = in.nextInt();
                holdings.put(symbol, qty);
            }
        } catch (IOException e) {
            System.out.println("Error loading portfolio: " + e.getMessage());
        }
    }
}

public class StockTradingPlatform {
    static Map<String, Stock> market = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeMarket();
        Portfolio portfolio = new Portfolio(10000); // starting with $10,000
        portfolio.loadFromFile("portfolio.txt");

        while (true) {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    displayMarket();
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int buyQty = scanner.nextInt();
                    if (market.containsKey(buySymbol)) {
                        portfolio.buyStock(market.get(buySymbol), buyQty);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int sellQty = scanner.nextInt();
                    if (market.containsKey(sellSymbol)) {
                        portfolio.sellStock(market.get(sellSymbol), sellQty);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 4:
                    portfolio.showPortfolio(market);
                    break;
                case 5:
                    portfolio.showHistory();
                    break;
                case 6:
                    portfolio.saveToFile("portfolio.txt");
                    System.out.println("Portfolio saved. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 180.00));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2500.00));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 750.00));
        market.put("AMZN", new Stock("AMZN", "Amazon.com", 3300.00));
    }

    private static void displayMarket() {
        System.out.println("---- Market Data ----");
        for (Stock stock : market.values()) {
            System.out.printf("%s (%s): $%.2f\n", stock.symbol, stock.name, stock.price);
        }
    }
}
