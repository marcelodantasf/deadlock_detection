import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/*Ao iniciar a execução da simulação, o programa deverá solicitar ao usuário o
intervalo △t (em segundos), e em seguida instanciar o thread sistema operacional. */

public class OSConfigScreen extends JFrame {
    private JTextField verificationIntervalField;

    public OSConfigScreen() {
        setTitle("Configuração do S.O.");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel verificationIntervalLabel = new JLabel("Intervalo entre checagens (s)");
        verificationIntervalField = new JTextField(10);
        addPlaceholder(verificationIntervalField, "Ex: 5");

        JButton nextButton = new JButton("Próximo");
        nextButton.addActionListener((ActionEvent e) -> {
            int verificationInterval = Integer.parseInt(verificationIntervalField.getText());
            try {
                if(verificationIntervalField.getText().equals("Ex: 5")){
                    JOptionPane.showMessageDialog(this, "Preencha o campo corretamente");
                    return;
                }

                if(verificationInterval <= 0){
                    JOptionPane.showMessageDialog(this, "Insira valores válidos");
                    return;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Insira valores válidos");
                return;
            }

            OS os = new OS(verificationInterval);
            ResourceConfigScreen resourceConfig = new ResourceConfigScreen(os);
            resourceConfig.setVisible(true);
            dispose();
        });

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(verificationIntervalLabel);
        panel.add(verificationIntervalField);
        panel.add(nextButton);

        add(panel);

        getRootPane().setDefaultButton(nextButton);
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }
}
