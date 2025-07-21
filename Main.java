import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> {
        //     OSConfigScreen osConfig = new OSConfigScreen();
        //     osConfig.setVisible(true);
        // });

        System.out.println("\n\n=== TESTE SIMPLES DE PROCESSO E RECURSOS ===\n");
        
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("Impressora", 1, 1));
        resources.add(new Resource("Scanner", 2, 1));
        
        Process processo = new Process(1, 3, 10, resources);
        
        System.out.println("Recursos criados:");
        resources.forEach(r -> System.out.println(" - " + r));
        System.out.println("\nIniciando processo único...");
        
        processo.start();
        
    }
}