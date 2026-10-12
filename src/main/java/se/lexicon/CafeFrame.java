package se.lexicon;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

public class CafeFrame extends JFrame {

    private static final String[] ITEM_NAMES = {"Espresso", "Cappuccino", "Latte", "Croissant", "Sandwich"};
    private static final double[] ITEM_PRICES = {25.00, 35.00, 40.00, 30.00, 55.00};

    private static final Color BG_COLOR = new Color(0xFBF4EC);
    private static final Color CARD_BG = new Color(0xFFFFFF);
    private static final Color PRIMARY = new Color(0x6F4E37);
    private static final Color ACCENT = new Color(0xD98C3D);
    private static final Color DANGER = new Color(0xB5542D);
    private static final Color TEXT_DARK = new Color(0x3A2A20);
    private static final Color MUTED_TEXT = new Color(0x8C7B6F);
    private static final Color BORDER_COLOR = new Color(0xE9DFD3);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font ITALIC_FONT = new Font("Segoe UI", Font.ITALIC, 13);

    private final Order order = new Order();
    private int customersServed = 0;
    private double totalRevenue = 0.0;

    private JTextField nameField;
    private JLabel greetingLabel;
    private JCheckBox memberCheck;
    private JTable menuTable;
    private JTable orderTable;
    private JSpinner qtySpinner;
    private ModernButton addButton;
    private ModernButton checkoutButton;
    private JLabel subtotalValue;
    private JLabel discountValue;
    private JLabel vatValue;
    private JLabel totalValue;
    private JPanel discountRow;
    private JLabel dayStatsLabel;

    public CafeFrame() {
        super("Lexicon Cafe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setMinimumSize(new Dimension(880, 600));
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG_COLOR);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(0, 18));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        refreshTotals();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Lexicon Cafe");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY);

        JLabel subtitle = new JLabel("Order coffee, pastries, and more");
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(MUTED_TEXT);

        header.add(title);
        header.add(subtitle);
        return header;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new java.awt.GridLayout(1, 2, 18, 0));
        center.setOpaque(false);
        center.add(buildOrderFormCard());
        center.add(buildCurrentOrderCard());
        return center;
    }

    private JComponent buildOrderFormCard() {
        RoundedPanel card = new RoundedPanel(CARD_BG, 20);
        card.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(sectionTitle("Customer & Menu"));
        content.add(Box.createVerticalStrut(14));

        content.add(fieldLabel("Customer name"));
        content.add(Box.createVerticalStrut(4));
        nameField = new JTextField();
        styleTextField(nameField);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateGreeting();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateGreeting();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateGreeting();
            }
        });
        content.add(nameField);
        content.add(Box.createVerticalStrut(6));

        greetingLabel = new JLabel("Welcome! Please enter your name to begin.");
        greetingLabel.setFont(ITALIC_FONT);
        greetingLabel.setForeground(MUTED_TEXT);
        content.add(greetingLabel);
        content.add(Box.createVerticalStrut(16));

        memberCheck = new JCheckBox("Loyalty member");
        memberCheck.setOpaque(false);
        memberCheck.setFont(BODY_FONT);
        memberCheck.setForeground(TEXT_DARK);
        memberCheck.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        memberCheck.addActionListener(e -> {
            order.member = memberCheck.isSelected();
            refreshTotals();
        });
        content.add(memberCheck);
        content.add(Box.createVerticalStrut(16));

        content.add(fieldLabel("Menu - select an item"));
        content.add(Box.createVerticalStrut(6));

        menuTable = new JTable(new MenuTableModel());
        styleTable(menuTable);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.getSelectionModel().setSelectionInterval(0, 0);
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        menuScroll.setPreferredSize(new Dimension(100, 150));
        content.add(menuScroll);
        content.add(Box.createVerticalStrut(14));

        JPanel addRow = new JPanel();
        addRow.setOpaque(false);
        addRow.setLayout(new BoxLayout(addRow, BoxLayout.X_AXIS));
        addRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel qtyLabel = fieldLabel("Qty");
        addRow.add(qtyLabel);
        addRow.add(Box.createHorizontalStrut(8));
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        qtySpinner.setMaximumSize(new Dimension(70, 32));
        qtySpinner.setFont(BODY_FONT);
        addRow.add(qtySpinner);
        addRow.add(Box.createHorizontalStrut(16));
        addButton = new ModernButton("Add to Order", ACCENT, ACCENT.brighter());
        addButton.addActionListener(this::onAddItem);
        addRow.add(addButton);
        content.add(addRow);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildCurrentOrderCard() {
        RoundedPanel card = new RoundedPanel(CARD_BG, 20);
        card.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        content.setLayout(new BorderLayout(0, 14));

        content.add(sectionTitle("Current Order"), BorderLayout.NORTH);

        orderTable = new JTable(new OrderTableModel());
        styleTable(orderTable);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        content.add(orderScroll, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        subtotalValue = valueLabel(false);
        discountValue = valueLabel(false);
        vatValue = valueLabel(false);
        totalValue = valueLabel(true);

        south.add(totalsRow("Subtotal", subtotalValue));
        discountRow = totalsRow("Discount", discountValue);
        south.add(discountRow);
        south.add(totalsRow("VAT", vatValue));
        south.add(Box.createVerticalStrut(6));
        south.add(separator());
        south.add(Box.createVerticalStrut(6));
        south.add(totalsRow("TOTAL", totalValue));
        south.add(Box.createVerticalStrut(16));

        checkoutButton = new ModernButton("Checkout", PRIMARY, PRIMARY.brighter());
        checkoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkoutButton.addActionListener(this::onCheckout);
        south.add(checkoutButton);

        content.add(south, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        dayStatsLabel = new JLabel("Today: 0 customers · 0.00 SEK");
        dayStatsLabel.setFont(BODY_FONT);
        dayStatsLabel.setForeground(MUTED_TEXT);
        footer.add(dayStatsLabel, BorderLayout.WEST);

        ModernButton endDayButton = new ModernButton("End Day & Exit", DANGER, DANGER.brighter());
        endDayButton.addActionListener(this::onEndDay);
        footer.add(endDayButton, BorderLayout.EAST);

        return footer;
    }

    private void onAddItem(java.awt.event.ActionEvent e) {
        int row = menuTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item from the menu.",
                    "No item selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int quantity = (Integer) qtySpinner.getValue();
        order.addItem(ITEM_NAMES[row], ITEM_PRICES[row], quantity);
        ((AbstractTableModel) orderTable.getModel()).fireTableDataChanged();
        refreshTotals();
        qtySpinner.setValue(1);
    }

    private void onCheckout(java.awt.event.ActionEvent e) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer's name.",
                    "Missing name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (order.items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one item before checking out.",
                    "Empty order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        order.customerName = name;
        showReceiptDialog();

        customersServed++;
        totalRevenue += order.calculateTotal();
        updateDayStats();

        order.items.clear();
        order.member = false;
        memberCheck.setSelected(false);
        nameField.setText("");
        ((AbstractTableModel) orderTable.getModel()).fireTableDataChanged();
        refreshTotals();
    }

    private void onEndDay(java.awt.event.ActionEvent e) {
        JDialog dialog = new JDialog(this, "End of Day Report", true);
        dialog.setSize(380, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_COLOR);
        dialog.setLayout(new BorderLayout());
        ((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        RoundedPanel card = new RoundedPanel(CARD_BG, 20);
        card.setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("End of Day Report");
        title.setFont(SECTION_FONT);
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(18));
        content.add(separator());
        content.add(Box.createVerticalStrut(12));

        content.add(totalsRow("Customers served", valueLabel(false, String.valueOf(customersServed))));
        content.add(totalsRow("Total revenue", valueLabel(false, money(totalRevenue))));
        content.add(Box.createVerticalStrut(18));

        ModernButton exitButton = new ModernButton("Exit", DANGER, DANGER.brighter());
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(ev -> System.exit(0));
        content.add(exitButton);

        card.add(content, BorderLayout.CENTER);
        dialog.add(card, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showReceiptDialog() {
        JDialog dialog = new JDialog(this, "Receipt", true);
        dialog.setSize(420, 560);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_COLOR);
        dialog.setLayout(new BorderLayout());
        ((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        RoundedPanel card = new RoundedPanel(CARD_BG, 20);
        card.setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Lexicon Cafe");
        title.setFont(SECTION_FONT.deriveFont(20f));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);

        JLabel sub = new JLabel("Receipt");
        sub.setFont(ITALIC_FONT);
        sub.setForeground(MUTED_TEXT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(sub);
        content.add(Box.createVerticalStrut(16));

        content.add(totalsRow("Customer", valueLabel(false, order.customerName)));
        content.add(Box.createVerticalStrut(6));
        content.add(separator());
        content.add(Box.createVerticalStrut(6));

        for (LineItem item : order.items) {
            content.add(totalsRow(item.itemName + " x" + item.quantity, valueLabel(false, money(item.lineTotal()))));
        }

        content.add(Box.createVerticalStrut(6));
        content.add(separator());
        content.add(Box.createVerticalStrut(6));

        double subtotal = order.calculateSubtotal();
        double discount = order.calculateDiscount();
        double vat = order.calculateVat();
        double total = order.calculateTotal();

        content.add(totalsRow("Subtotal", valueLabel(false, money(subtotal))));
        if (discount > 0) {
            content.add(totalsRow("Discount", valueLabel(false, "-" + money(discount))));
        }
        content.add(totalsRow("VAT", valueLabel(false, money(vat))));
        content.add(Box.createVerticalStrut(6));
        content.add(separator());
        content.add(Box.createVerticalStrut(6));
        content.add(totalsRow("TOTAL", valueLabel(true, money(total))));
        content.add(Box.createVerticalStrut(20));

        JLabel thanks = new JLabel("Thank you, " + order.customerName + "!");
        thanks.setFont(ITALIC_FONT);
        thanks.setForeground(TEXT_DARK);
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(thanks);

        JLabel seeYou = new JLabel("See you next time.");
        seeYou.setFont(ITALIC_FONT);
        seeYou.setForeground(TEXT_DARK);
        seeYou.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(seeYou);
        content.add(Box.createVerticalStrut(16));

        ModernButton closeButton = new ModernButton("Close", PRIMARY, PRIMARY.brighter());
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(ev -> dialog.dispose());
        content.add(closeButton);

        card.add(content, BorderLayout.CENTER);
        dialog.add(card, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void refreshTotals() {
        double subtotal = order.calculateSubtotal();
        double discount = order.calculateDiscount();
        double vat = order.calculateVat();
        double total = order.calculateTotal();

        subtotalValue.setText(money(subtotal));
        discountValue.setText("-" + money(discount));
        discountRow.setVisible(discount > 0);
        vatValue.setText(money(vat));
        totalValue.setText(money(total));
        checkoutButton.setEnabled(!order.items.isEmpty());
    }

    private void updateGreeting() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            greetingLabel.setText("Welcome! Please enter your name to begin.");
        } else {
            greetingLabel.setText("Hi " + name + "! What would you like today?");
        }
    }

    private void updateDayStats() {
        String customerWord = customersServed == 1 ? "customer" : "customers";
        dayStatsLabel.setText("Today: " + customersServed + " " + customerWord + " · " + money(totalRevenue));
    }

    private static String money(double amount) {
        return String.format(Locale.US, "%.2f SEK", amount);
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION_FONT);
        label.setForeground(PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(MUTED_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel valueLabel(boolean emphasised) {
        return valueLabel(emphasised, "0.00 SEK");
    }

    private JLabel valueLabel(boolean emphasised, String text) {
        JLabel label = new JLabel(text);
        label.setFont(emphasised ? SECTION_FONT.deriveFont(16f) : BODY_FONT.deriveFont(Font.BOLD, 14f));
        label.setForeground(emphasised ? ACCENT : TEXT_DARK);
        return label;
    }

    private JPanel totalsRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(fieldLabel(labelText), BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private JComponent separator() {
        JPanel line = new JPanel();
        line.setBackground(BORDER_COLOR);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(10, 1));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        return line;
    }

    private void styleTextField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(26);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(BODY_FONT.deriveFont(Font.BOLD, 13f));
        table.getTableHeader().setBackground(new Color(0xF4ECE1));
        table.getTableHeader().setForeground(PRIMARY);
        table.setSelectionBackground(ACCENT.brighter());
        table.setSelectionForeground(Color.WHITE);
    }

    private class MenuTableModel extends AbstractTableModel {
        private final String[] columns = {"Item", "Price"};

        @Override
        public int getRowCount() {
            return ITEM_NAMES.length;
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            return column == 0 ? ITEM_NAMES[row] : money(ITEM_PRICES[row]);
        }
    }

    private class OrderTableModel extends AbstractTableModel {
        private final String[] columns = {"Item", "Qty", "Line Total"};

        @Override
        public int getRowCount() {
            return order.items.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            LineItem item = order.items.get(row);
            switch (column) {
                case 0:
                    return item.itemName;
                case 1:
                    return "x" + item.quantity;
                default:
                    return money(item.lineTotal());
            }
        }
    }
}
