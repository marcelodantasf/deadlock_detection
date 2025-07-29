import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OSConfigScreen osConfig = new OSConfigScreen();
            osConfig.setVisible(true);
        });
    }
}