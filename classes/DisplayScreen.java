import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

public class DisplayScreen extends JFrame{
    private ArrayList<Resource> resourceList;
    private ArrayList<Process> processList;
    private JLayeredPane layeredPane;
    private JTextArea logArea;
    private JTable resourceTable;
    private JTable processTable;
    private JButton killProcessButton;
    private JTextField processIdField;
    private JFrame createProcessFrame;

    public int processIdCount = 0;
    public static Semaphore ProcessCount;

    public DisplayScreen(ArrayList<Resource> resources, OS os) {
        setTitle("Simulação");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ProcessCount = new Semaphore(0, true);
         
        // Create main panels
        JPanel controlPanel = createControlPanel();
        JPanel statusPanel = createStatusPanel();
        JPanel logPanel = createLogPanel();
        
        // Add panels to frame
        add(controlPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        os.setDisplayScreen(this);
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
        killProcessButton.addActionListener(e -> System.out.println("[TO DO]: matar processo"));
        
        panel.add(addProcessButton);
        panel.add(killLabel);
        panel.add(processIdField);
        panel.add(killProcessButton);
        
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        // Process table
        String[] processColumns = {"ID", "Status", "Recursos Usados", "Recursos Esperados"};
        DefaultTableModel processModel = new DefaultTableModel(processColumns, 0);
        processTable = new JTable(processModel);
        JScrollPane processScroll = new JScrollPane(processTable);
        
        // Resource table
        String[] resourceColumns = {"ID", "Nome", "Total", "Disponíveis", "Alocados"};
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
        CreateProcessPopUp popup = new CreateProcessPopUp(this, processIdCount);
        popup.setVisible(true);
        
        /*Process newProcess = popup.getCreatedProcess();
        if (newProcess != null) {
            processList.add(newProcess);
            newProcess.start();
        }*/
    }
}
    //TODO: createProcess class as pop up
