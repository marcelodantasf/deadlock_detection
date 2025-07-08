import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConfigScreen config = new ConfigScreen();
            config.setVisible(true);
        });
    }
}