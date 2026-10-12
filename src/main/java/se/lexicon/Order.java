package se.lexicon;

import java.util.ArrayList;
import java.util.Locale;

public class Order {

    private static final double MEMBER_DISCOUNT_RATE = 0.15;
    private static final double LARGE_ORDER_DISCOUNT_RATE = 0.10;
    private static final double LARGE_ORDER_THRESHOLD = 150.00;
    private static final double VAT_RATE = 0.12;

    public String customerName;
    public boolean member;
    public ArrayList<LineItem> items = new ArrayList<>();

    public void addItem(String itemName, double unitPrice, int quantity) {
        LineItem lineItem = new LineItem();
        lineItem.itemName = itemName;
        lineItem.unitPrice = unitPrice;
        lineItem.quantity = quantity;
        items.add(lineItem);
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (LineItem item : items) {
            subtotal += item.lineTotal();
        }
        return subtotal;
    }

    public double calculateDiscount() {
        double subtotal = calculateSubtotal();
        if (member) {
            return subtotal * MEMBER_DISCOUNT_RATE;
        }
        if (subtotal > LARGE_ORDER_THRESHOLD) {
            return subtotal * LARGE_ORDER_DISCOUNT_RATE;
        }
        return 0.0;
    }

    public double calculateVat() {
        return (calculateSubtotal() - calculateDiscount()) * VAT_RATE;
    }

    public double calculateTotal() {
        return calculateSubtotal() - calculateDiscount() + calculateVat();
    }

    public void printReceipt() {
        double subtotal = calculateSubtotal();
        double discount = calculateDiscount();
        double vat = calculateVat();
        double total = calculateTotal();

        IO.println("");
        IO.println("=".repeat(30));
        IO.println("      LEXICON CAFE");
        IO.println("=".repeat(30));
        printLine("Customer", customerName);
        IO.println("-".repeat(30));
        for (LineItem item : items) {
            IO.println(String.format(Locale.US, "  %-16sx%-5d%.2f SEK", item.itemName, item.quantity, item.lineTotal()));
        }
        IO.println("-".repeat(30));
        printLine("Subtotal", String.format(Locale.US, "%.2f SEK", subtotal));
        if (discount > 0) {
            printLine("Discount", String.format(Locale.US, "-%.2f SEK", discount));
        }
        printLine("VAT", String.format(Locale.US, "%.2f SEK", vat));
        IO.println("-".repeat(30));
        printLine("TOTAL", String.format(Locale.US, "%.2f SEK", total));
        IO.println("=".repeat(30));
        IO.println("   Thank you, " + customerName + "!");
        IO.println("   See you next time.");
        IO.println("=".repeat(30));
    }

    private void printLine(String label, String value) {
        IO.println(String.format("%-10s: %s", label, value));
    }
}
