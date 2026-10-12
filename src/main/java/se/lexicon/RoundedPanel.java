package se.lexicon;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundedPanel extends JPanel {

    private final Color background;
    private final int radius;

    public RoundedPanel(Color background, int radius) {
        this.background = background;
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(background);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
