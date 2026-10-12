package se.lexicon;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class CafeApp {

    static void main() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception ignored) {
            // fall back to the platform default look and feel
        }

        SwingUtilities.invokeLater(() -> new CafeFrame().setVisible(true));
    }
}
