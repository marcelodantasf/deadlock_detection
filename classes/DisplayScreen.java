import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

public class DisplayScreen extends JFrame{
    private ArrayList<Resource> resourceList;
    private ArrayList<Process> processList = new ArrayList<Process>();
    private JTextArea logArea;
    private JTable resourceTable;
    private JTable processTable;
    private JButton killProcessButton;
    private JTextField processIdField;
    private OS os;

    public int processIdCount = 0;
    public static Semaphore ProcessCount;

    public DisplayScreen(ArrayList<Resource> resourceList, OS os) {
        setTitle("Simulação");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ProcessCount = new Semaphore(0, true);
        this.resourceList = resourceList;
        this.os = os;
         
        // Create main panels
        JPanel controlPanel = createControlPanel();
        JPanel statusPanel = createStatusPanel();
        JPanel logPanel = createLogPanel();
        
        // Add panels to frame
        add(controlPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        os.setDisplayScreen(this);
        new Thread(this::updateDisplay).start();
        os.start();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        // Add process button
        JButton addProcessButton = new JButton("Adicionar Processo");
        addProcessButton.addActionListener(e -> openNewProcessPopUp());
        
        // Kill process components
        JLabel killLabel = new JLabel("Matar Processo ID:");
        processIdField = new JTextField(5);
        killProcessButton = new JButton("Matar");
        killProcessButton.addActionListener(e -> {
            int processToKill = Integer.parseInt(processIdField.getText());
            processList.get(processToKill - 1).kill();
            log("Processo " + processToKill + " interrompido");
        });
        
        panel.add(addProcessButton);
        panel.add(killLabel);
        panel.add(processIdField);
        panel.add(killProcessButton);
        
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        // Process table
        String[] processColumns = {"ID do Processo", "Status", "Recursos Usados", "Recursos Esperados"};
        DefaultTableModel processModel = new DefaultTableModel(processColumns, 0);
        processTable = new JTable(processModel);
        JScrollPane processScroll = new JScrollPane(processTable);
        
        // Resource table
        String[] resourceColumns = {"ID do Recurso", "Nome", "Total", "Disponíveis", "Alocados"};
        DefaultTableModel resourceModel = new DefaultTableModel(resourceColumns, 0);
        resourceTable = new JTable(resourceModel);
        JScrollPane resourceScroll = new JScrollPane(resourceTable);
        
        panel.add(processScroll);
        panel.add(resourceScroll);
        
        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 200));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        // Auto-scroll
        DefaultCaret caret = (DefaultCaret)logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane scroll = new JScrollPane(logArea);
        panel.add(new JLabel("Log de Operações:"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }

    private void openNewProcessPopUp() {
        CreateProcessPopUp popup = new CreateProcessPopUp(this, processIdCount, this.resourceList, this.processList, this.os);
        popup.setVisible(true);
    }

     private void updateDisplay() {
        while (true) {
            try {
                Thread.sleep(500); // Update twice per second
                
                // Update process table
                DefaultTableModel processModel = (DefaultTableModel)processTable.getModel();
                processModel.setRowCount(0);
                
                for (Process p : processList) {
                    String status = p.getState().toString().equals("TIMED_WAITING") ? "Rodando" : "Bloqueado";
                    if (!p.isRunning()) status = "Interrompido";
                    processModel.addRow(new Object[]{
                        p.getProcessId(),
                        status,
                        p.getResourceBeingUsedList(), // Replace with data being used
                        p.getResourceRequested() // Replace with data request
                    });
                }
                
                // Update resource table
                DefaultTableModel resourceModel = (DefaultTableModel)resourceTable.getModel();
                resourceModel.setRowCount(0);
                
                for (Resource r : resourceList) {
                    int available = r.currentInstances.availablePermits();
                    int allocated = r.getMaxInstances() - available;
                    
                    resourceModel.addRow(new Object[]{
                        r.getId(),
                        r.getName(),
                        r.getMaxInstances(),
                        available,
                        allocated
                    });
                }
                
                // Check for deadlocks
                //checkDeadlocks();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + timestamp + "] " + message + "\n");
        });
    }
}
