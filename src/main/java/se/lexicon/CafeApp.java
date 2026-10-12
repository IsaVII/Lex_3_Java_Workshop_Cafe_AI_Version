package se.lexicon;

import java.util.Locale;

public class CafeApp {

    private static final String[] ITEM_NAMES = {"Espresso", "Cappuccino", "Latte", "Croissant", "Sandwich"};
    private static final double[] ITEM_PRICES = {25.00, 35.00, 40.00, 30.00, 55.00};

    static void main() {
        int customersServed = 0;
        double totalRevenue = 0.0;

        while (true) {
            String name = askName();
            if (name.trim().equalsIgnoreCase("done")) {
                break;
            }

            IO.println("Hi " + name + "! Here is our menu:");
            IO.println("");
            printMenu();

            boolean isMember = askLoyaltyMembership();
            IO.println("");

            Order order = new Order();
            order.customerName = name;
            order.member = isMember;

            while (true) {
                int itemIndex = askItemNumberOrFinish();
                if (itemIndex == -1) {
                    break;
                }
                int quantity = askQuantity();
                order.addItem(ITEM_NAMES[itemIndex], ITEM_PRICES[itemIndex], quantity);
                IO.println(ITEM_NAMES[itemIndex] + " added.");
                IO.println("");
            }

            order.printReceipt();

            customersServed++;
            totalRevenue += order.calculateTotal();
        }

        printEndOfDayReport(customersServed, totalRevenue);
    }

    private static String askName() {
        return IO.readln("Next customer name (or 'done' to close): ");
    }

    private static void printMenu() {
        IO.println("=".repeat(30));
        IO.println("       Lexicon Cafe");
        IO.println("=".repeat(30));
        for (int i = 0; i < ITEM_NAMES.length; i++) {
            String label = (i + 1) + ". " + ITEM_NAMES[i];
            IO.println(String.format(Locale.US, "%-20s%.2f SEK", label, ITEM_PRICES[i]));
        }
        IO.println("=".repeat(30));
        IO.println("");
    }

    private static int askItemNumberOrFinish() {
        while (true) {
            String input = IO.readln("Enter item number (1-5, or 0 to finish): ");
            try {
                int itemNumber = Integer.parseInt(input.trim());
                if (itemNumber < 0 || itemNumber > ITEM_NAMES.length) {
                    IO.println("Please enter a number between 0 and " + ITEM_NAMES.length + ".");
                    continue;
                }
                if (itemNumber == 0) {
                    return -1;
                }
                return itemNumber - 1;
            } catch (NumberFormatException e) {
                IO.println("Please enter a valid number.");
            }
        }
    }

    private static int askQuantity() {
        while (true) {
            String input = IO.readln("How many? ");
            try {
                int quantity = Integer.parseInt(input.trim());
                if (quantity <= 0) {
                    IO.println("Quantity must be greater than zero.");
                    continue;
                }
                return quantity;
            } catch (NumberFormatException e) {
                IO.println("Please enter a valid number.");
            }
        }
    }

    private static boolean askLoyaltyMembership() {
        while (true) {
            String input = IO.readln("Loyalty member? (yes/no): ").trim();
            if (input.equalsIgnoreCase("yes")) {
                return true;
            }
            if (input.equalsIgnoreCase("no")) {
                return false;
            }
            IO.println("Please enter 'yes' or 'no'.");
        }
    }

    private static void printEndOfDayReport(int customersServed, double totalRevenue) {
        IO.println("=".repeat(30));
        IO.println("      END OF DAY REPORT");
        IO.println("=".repeat(30));
        printSummaryLine("Customers served", String.valueOf(customersServed));
        printSummaryLine("Total revenue", String.format(Locale.US, "%.2f SEK", totalRevenue));
        IO.println("=".repeat(30));
    }

    private static void printSummaryLine(String label, String value) {
        IO.println(String.format("%-17s: %s", label, value));
    }
}
