
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CreateProcessPopUp extends JDialog {
    private JTextField reqIntervalField;
    private JTextField usageIntervalField;
    private Process createdProcess;
    private DisplayScreen displayScreen;

    public CreateProcessPopUp(DisplayScreen parent, int processIdCount) {
        super(parent, "Criar Novo Processo", true);
        this.displayScreen = parent;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel reqIntervalLabel = new JLabel("Intervalo entre requisições (s):");
        reqIntervalField = new JTextField();
        addPlaceholder(reqIntervalField, "Ex: 3");

        JLabel usageIntervalLabel = new JLabel("Intervalo de utilização (s):");
        usageIntervalField = new JTextField();
        addPlaceholder(usageIntervalField, "Ex: 2");

        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener((ActionEvent e) -> {
            try {
                //algo
            } catch (Exception ex) {
                //mensagem
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
        });

        add(reqIntervalLabel);
        add(reqIntervalField);
        add(usageIntervalLabel);
        add(usageIntervalField);
        add(addButton);
        add(cancelButton);

        getRootPane().setDefaultButton(addButton);
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
