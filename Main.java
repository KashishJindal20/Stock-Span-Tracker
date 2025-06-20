import java.util.Scanner;
import java.io.Console;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        UserManager userManager = new UserManager();
        User currentUser = null;
        String username = "";

        while (currentUser == null) {
            System.out.println("\n============== Welcome to Real-Time Stock Tracker ==============");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("===============================================================");
            System.out.print("Choose an option (1 or 2): ");
            int option = scan.nextInt();
            scan.nextLine(); // consume newline

            System.out.print("Enter username: ");
            username = scan.nextLine();

            Console console = System.console();
            String password = "";

            if (console != null) {
                char[] passwordChars = console.readPassword("Enter password: ");
                password = new String(passwordChars);
            } else {
           
                System.out.print("Enter password (input visible): ");
                password = scan.nextLine();
            }

            if (option == 1) {
                boolean success = userManager.registerUser(username, password);
                if (success) {
                    System.out.println("Registration successful!");
                    currentUser = userManager.authenticateUser(username, password);
                    currentUser.getTracker().loadFromFile(username + ".txt");
                } else {
                    System.out.println("Username already exists. Try logging in.");
                }
            } else if (option == 2) {
                currentUser = userManager.authenticateUser(username, password);
                if (currentUser != null) {
                    System.out.println("Login successful! Welcome back, " + username);
                    currentUser.getTracker().loadFromFile(username + ".txt");
                } else {
                    System.out.println("Invalid credentials.");
                }
            } else {
                System.out.println("Invalid option.");
            }

            if (currentUser == null) {
                System.out.print("Do you want to try again? (yes/no): ");
                String choice = scan.nextLine();
                if (!choice.equalsIgnoreCase("yes")) {
                    System.out.println("Exiting...");
                    return;
                }
            }
        }

        StockTracker tracker = currentUser.getTracker();

        while (true) {
            System.out.println("\n====================== MAIN MENU ======================");
            System.out.println("1. Add Price");
            System.out.println("2. View Prices & Span");
            System.out.println("3. Update Price");
            System.out.println("4. Delete Price");
            System.out.println("5. View Price Analysis");
            System.out.println("6. Exit");
            System.out.println("=======================================================");
            System.out.print("Choose an option: ");
            int choice = scan.nextInt();
            System.out.println(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter day number: ");
                    int day = scan.nextInt();
                    System.out.println(); 
                    System.out.print("Enter stock price: ");
                    int price = scan.nextInt();
                    System.out.println(); 
                    tracker.addPriceOnDay(day, price);
                    break;

                case 2:
                    tracker.viewPricesWithSpan();
                    break;

                case 3:
                    System.out.print("Enter day to update: ");
                    int dayToUpdate = scan.nextInt();
                    System.out.println(); 
                    System.out.print("Enter new price: ");
                    int newPrice = scan.nextInt();
                    System.out.println(); 
                    tracker.updatePrice(dayToUpdate, newPrice);
                    break;

                case 4:
                    System.out.print("Enter day to delete: ");
                    int dayToDelete = scan.nextInt();
                    tracker.deletePrice(dayToDelete);
                    break;

                case 5:
                    displayPriceAnalysis(tracker);
                    break;

                case 6:
                    currentUser.getTracker().saveToFile(username + ".txt");
                    System.out.println("Data saved to " + username + ".txt");
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void displayPriceAnalysis(StockTracker tracker) {
        double avgPrice = tracker.getAveragePrice();
        StockEntry highest = tracker.getHighestPrice();
        StockEntry lowest = tracker.getLowestPrice();
        double stdDev = tracker.getStandardDeviation();
        double volatility = tracker.getPercentageVolatility();

        System.out.println("\n************* PRICE ANALYSIS REPORT *************");
        System.out.printf("%-25s : %.2f\n", "Average Price", avgPrice);
        if (highest != null) {
            System.out.printf("%-25s : %d (Day %d)\n", "Highest Price", highest.getPrice(), highest.getDay());
        }
        if (lowest != null) {
            System.out.printf("%-25s : %d (Day %d)\n", "Lowest Price", lowest.getPrice(), lowest.getDay());
        }
        System.out.printf("%-25s : %.2f\n", "Standard Deviation", stdDev);
        System.out.printf("%-25s : %.2f%%\n", "Percentage Volatility", volatility);
        System.out.println("*************************************************\n");
    }
}
