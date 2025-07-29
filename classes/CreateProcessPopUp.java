import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CreateProcessPopUp extends JDialog {
    private JTextField reqIntervalField;
    private JTextField usageIntervalField;
    private Process createdProcess;
    private DisplayScreen displayScreen;
    private OS os;

    public CreateProcessPopUp(DisplayScreen parent, int processIdCount, List<Resource> resources, ArrayList<Process> processList, OS os) {
        super(parent, "Criar Novo Processo", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.displayScreen = parent;
        this.os = os;

        JLabel reqIntervalLabel = new JLabel("Intervalo entre requisições (s):");
        reqIntervalField = new JTextField();
        addPlaceholder(reqIntervalField, "Ex: 3");

        JLabel usageIntervalLabel = new JLabel("Intervalo de utilização (s):");
        usageIntervalField = new JTextField();
        addPlaceholder(usageIntervalField, "Ex: 2");

        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener((ActionEvent e) -> {
            handleAddProcess(resources, processList);
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

    private void handleAddProcess(List<Resource> resources, ArrayList<Process> processList) {
        try {
            int reqInterval = Integer.parseInt(reqIntervalField.getText());
            int usageInterval = Integer.parseInt(usageIntervalField.getText());

            if (reqInterval <= 0 || usageInterval <= 0) {
                JOptionPane.showMessageDialog(this, "Os intervalos devem ser maiores que zero", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and return the new process
            displayScreen.processIdCount++;
            
            this.createdProcess = new Process(displayScreen.processIdCount, reqInterval, usageInterval, resources);
            createdProcess.setDisplayScreen(displayScreen);
            displayScreen.log("Processo " + displayScreen.processIdCount + " criado (Req: " + reqInterval + "s, Uso: " + usageInterval + "s)");
            displayScreen.log(createdProcess.toString());

            processList.add(createdProcess);
            os.addProcess(createdProcess);
            createdProcess.start();

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Process getCreatedProcess() {
        return this.createdProcess;
    }
}
