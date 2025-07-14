import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

/*Antes de iniciar a simulação, o usuário deverá informar todos os tipos de recursos existentes
no sistema, e para cada tipo de recurso informado o usuário deverá configurar os seguintes
parâmetros:
• Nome do Recurso (Ex: Impressora)
• Identificador do Recurso (Ex: 1)
• Quantidade de instâncias do recurso (Ex: 5)
Obs.: O número máximo de tipos de recursos é 10.*/

public class ConfigScreen extends JFrame{
    private JTextField nameResourceField;
    //private JTextField idResourceField;
    private JTextField maxInstancesField;

    public int idCount = 0;
    public int resourceTypeCount = 0;
    public ArrayList<Recurso> resources;

    public ConfigScreen() {
        setTitle("Configuração Inicial");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        resources = new ArrayList<Recurso>();

        JLabel nameResourceLabel = new JLabel("Nome do recurso:");
        nameResourceField = new JTextField(10);
        addPlaceholder(nameResourceField, "Ex: Impressora");

        JLabel maxInstancesLabel = new JLabel("Instâncias disponíveis:");
        maxInstancesField = new JTextField(10);
        addPlaceholder(maxInstancesField, "Ex: 5");

        JButton startButton = new JButton("Iniciar");

        startButton.addActionListener((ActionEvent e) -> {
            try {
                if(resources.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Nenhum recurso adicionado.");
                    return;
                }
                /*ExhibitionScreen exhibition = new ExhibitionScreen(resources);
                exhibition.setVisible(true);*/
                System.out.println();
                for(int i = 0; i< resources.size(); i++){
                    System.out.println("recursos cadastrados: " + resources.get(i).toString());
                }
                dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Insira valores válidos.");
            }
        });

        JButton addButton = new JButton("Adicionar");

        addButton.addActionListener((ActionEvent e) -> {
            try {
                if(nameResourceField.getText().equals("Ex: Impressora") || maxInstancesField.getText().equals("Ex: 5")) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
                    return;
                }

                int maxInstances = Integer.parseInt(maxInstancesField.getText());
                if (maxInstances <= 0) {
                    JOptionPane.showMessageDialog(this, "Os valores devem ser maiores que zero.");
                    return;
                }

                idCount +=1 ;
                Recurso r = new Recurso(nameResourceField.getText(), idCount, maxInstances);
                resources.add(r);
                System.out.println("[ADD]: " + resources.get(idCount-1).toString());

                /*ExhibitionScreen exhibition = new ExhibitionScreen(resources);
                exhibition.setVisible(true);*/
                nameResourceField.setText("");
                maxInstancesField.setText("");

                addPlaceholder(nameResourceField, "Ex: Impressora");
                addPlaceholder(maxInstancesField, "Ex: 5");

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Insira valores válidos.");
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(nameResourceLabel);
        panel.add(nameResourceField);
        panel.add(maxInstancesLabel);
        panel.add(maxInstancesField);
        panel.add(new JLabel()); // Espaço vazio
        panel.add(startButton);
        panel.add(addButton);

        add(panel);

        getRootPane().setDefaultButton(startButton);
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
