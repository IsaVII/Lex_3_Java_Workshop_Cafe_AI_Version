package se.lexicon;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    private final Color baseColor;
    private final Color hoverColor;
    private boolean hovering = false;

    public ModernButton(String text, Color baseColor, Color hoverColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 22, 10, 22));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fill = isEnabled() ? (hovering ? hoverColor : baseColor) : new Color(0xC8C2BC);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        g2.dispose();
        super.paintComponent(g);
    }
}
