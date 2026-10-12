package se.lexicon;

import java.util.Locale;

public class CafeApp {

    private static final String[] ITEM_NAMES = {"Espresso", "Cappuccino", "Latte", "Croissant", "Sandwich"};
    private static final double[] ITEM_PRICES = {25.00, 35.00, 40.00, 30.00, 55.00};

    private static final double MEMBER_DISCOUNT_RATE = 0.15;
    private static final double LARGE_ORDER_DISCOUNT_RATE = 0.10;
    private static final double LARGE_ORDER_THRESHOLD = 150.00;
    private static final double VAT_RATE = 0.12;

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

            int itemIndex = askItemNumber();
            int quantity = askQuantity();
            boolean isMember = askLoyaltyMembership();

            double subtotal = calculateSubtotal(ITEM_PRICES[itemIndex], quantity);
            double discount = calculateDiscount(subtotal, isMember);
            double vat = calculateVat(subtotal - discount);
            double total = calculateTotal(subtotal, discount, vat);

            printReceipt(name, ITEM_NAMES[itemIndex], quantity, subtotal, discount, vat, total);

            customersServed++;
            totalRevenue += total;
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

    private static int askItemNumber() {
        String input = IO.readln("Enter item number (1-5): ");
        return Integer.parseInt(input.trim()) - 1;
    }

    private static int askQuantity() {
        String input = IO.readln("How many? ");
        return Integer.parseInt(input.trim());
    }

    private static boolean askLoyaltyMembership() {
        String input = IO.readln("Loyalty member? (yes/no): ");
        return input.trim().equalsIgnoreCase("yes");
    }

    private static double calculateSubtotal(double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

    private static double calculateDiscount(double subtotal, boolean isMember) {
        if (isMember) {
            return subtotal * MEMBER_DISCOUNT_RATE;
        }
        if (subtotal > LARGE_ORDER_THRESHOLD) {
            return subtotal * LARGE_ORDER_DISCOUNT_RATE;
        }
        return 0.0;
    }

    private static double calculateVat(double amountAfterDiscount) {
        return amountAfterDiscount * VAT_RATE;
    }

    private static double calculateTotal(double subtotal, double discount, double vat) {
        return subtotal - discount + vat;
    }

    private static void printReceipt(String name, String itemName, int quantity,
                                       double subtotal, double discount, double vat, double total) {
        IO.println("");
        IO.println("=".repeat(30));
        IO.println("      LEXICON CAFE");
        IO.println("=".repeat(30));
        printLine("Customer", name);
        printLine("Item", itemName + " x " + quantity);
        printLine("Subtotal", String.format(Locale.US, "%.2f SEK", subtotal));
        if (discount > 0) {
            printLine("Discount", String.format(Locale.US, "-%.2f SEK", discount));
        }
        printLine("VAT", String.format(Locale.US, "%.2f SEK", vat));
        IO.println("-".repeat(30));
        printLine("TOTAL", String.format(Locale.US, "%.2f SEK", total));
        IO.println("=".repeat(30));
        IO.println("   Thank you, " + name + "!");
        IO.println("   See you next time.");
        IO.println("=".repeat(30));
    }

    private static void printLine(String label, String value) {
        IO.println(String.format("%-10s: %s", label, value));
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
