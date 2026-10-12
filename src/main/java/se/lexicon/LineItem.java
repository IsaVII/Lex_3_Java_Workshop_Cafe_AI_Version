package se.lexicon;

public class LineItem {

    public String itemName;
    public double unitPrice;
    public int quantity;

    public double lineTotal() {
        return unitPrice * quantity;
    }
}
