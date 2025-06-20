import java.io.*;
import java.util.*;

public class StockTracker {
    private List<StockEntry> stockList = new ArrayList<>();

    public double getAveragePrice() {
        if (stockList.isEmpty()) return 0;
        double sum = 0;
        for (StockEntry entry : stockList) {
            sum += entry.getPrice();
        }
        return sum / stockList.size();
    }

    public StockEntry getHighestPrice() {
        if (stockList.isEmpty()) return null;
        StockEntry highest = stockList.get(0);
        for (StockEntry entry : stockList) {
            if (entry.getPrice() > highest.getPrice()) {
                highest = entry;
            }
        }
        return highest;
    }

    public StockEntry getLowestPrice() {
        if (stockList.isEmpty()) return null;
        StockEntry lowest = stockList.get(0);
        for (StockEntry entry : stockList) {
            if (entry.getPrice() < lowest.getPrice()) {
                lowest = entry;
            }
        }
        return lowest;
    }

    public double getStandardDeviation() {
        double mean = getAveragePrice();
        double sum = 0;
        for (StockEntry entry : stockList) {
            sum += Math.pow(entry.getPrice() - mean, 2);
        }
        return Math.sqrt(sum / stockList.size());
    }

    public double getPercentageVolatility() {
        double mean = getAveragePrice();
        double stdDev = getStandardDeviation();
        return (stdDev / mean) * 100;
    }

    public void addPriceOnDay(int day, int price) {
        if (day <= 0) {
            System.out.println("⚠️ Invalid day.");
            return;
        }
        if (day > stockList.size()) {
            for (int i = stockList.size(); i < day - 1; i++) {
                stockList.add(new StockEntry(i + 1, 0));
            }
            stockList.add(new StockEntry(day, price));
            System.out.println("Added price for day " + day);
        } else {
            stockList.set(day - 1, new StockEntry(day, price));
            System.out.println("Updated price on day " + day);
        }
    }

    public void viewPricesWithSpan() {
        int[] prices = stockList.stream().mapToInt(StockEntry::getPrice).toArray();
        int[] spans = calculateSpan(prices);

        System.out.println("\n================ STOCK PRICE SPAN =================");
        System.out.printf("| %-6s | %-10s | %-6s |\n", "Day", "Price", "Span");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("| %-6d | %-10d | %-6d |\n", i + 1, prices[i], spans[i]);
        }
        System.out.println("==================================================\n");
    }

    public void updatePrice(int day, int newPrice) {
        if (day > 0 && day <= stockList.size()) {
            stockList.get(day - 1).setPrice(newPrice);
            System.out.println("Price updated on day " + day);
        } else {
            System.out.println("Invalid day");
        }
    }

    public void deletePrice(int day) {
        if (day > 0 && day <= stockList.size()) {
            stockList.remove(day - 1);
            for (int i = 0; i < stockList.size(); i++) {
                stockList.set(i, new StockEntry(i + 1, stockList.get(i).getPrice()));
            }
            System.out.println("Deleted price on day " + day);
        } else {
            System.out.println("Invalid day");
        }
    }

    private int[] calculateSpan(int[] prices) {
        int n = prices.length;
        int[] span = new int[n];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && prices[stack.peek()] <= prices[i]) {
                stack.pop();
            }
            span[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());
            stack.push(i);
        }
        return span;
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            int[] prices = stockList.stream().mapToInt(StockEntry::getPrice).toArray();
            int[] spans = calculateSpan(prices);
            for (int i = 0; i < stockList.size(); i++) {
                StockEntry entry = stockList.get(i);
                writer.write("Day " + entry.getDay() + ": Price - " + entry.getPrice() + ", Span - " + spans[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        stockList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String[] details = parts[1].split(", ");
                    if (details.length == 2) {
                        int price = Integer.parseInt(details[0].split(" - ")[1]);
                        int day = stockList.size() + 1;
                        stockList.add(new StockEntry(day, price));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Fine if file doesn’t exist
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
