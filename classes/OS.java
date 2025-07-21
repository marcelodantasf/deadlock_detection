import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OS extends Thread{
    private final int verificationInterval;
    private DisplayScreen displayScreen;

    private List<Process> processes = new CopyOnWriteArrayList<>();
    private List<Resource> resources;
    
    public OS(int verificationInterval, List<Resource> resources){
        this.verificationInterval = verificationInterval;
        this.resources = resources;
    }

    private void verify() {
        int n = processes.size();      // número de processos
        int m = resources.size();      // número de recursos

        int[][] allocation = new int[n][m];
        int[][] request = new int[n][m];
        int[] available = new int[m];
        boolean[] finished = new boolean[n];

        // available
        for (int j = 0; j < m; j++) {
            available[j] = resources.get(j).getAvailableInstances();
        }

        // allocation e request
        for (int i = 0; i < n; i++) {
            Process p = processes.get(i);
            for (int j = 0; j < m; j++) {
                allocation[i][j] = p.getAllocated(j);
                request[i][j] = p.getRequested(j);
            }
        }

        // Algoritmo de detecção
        int[] work = available.clone();

        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (!finished[i]) {
                    boolean canProceed = true;
                    for (int j = 0; j < m; j++) {
                        if (request[i][j] > work[j]) {
                            canProceed = false;
                            break;
                        }
                    }

                    if (canProceed) {
                        for (int j = 0; j < m; j++) {
                            work[j] += allocation[i][j];
                        }
                        finished[i] = true;
                        changed = true;
                    }
                }
            }
        } while (changed);

        // Verificar processos não finalizados
        boolean deadlockDetected = false;
        for (int i = 0; i < n; i++) {
            if (!finished[i]) {
                deadlockDetected = true;
                System.out.println("[DEADLOCK DETECTED]: " + processes.get(i).getName());
            }
        }

        if (!deadlockDetected) {
            System.out.println("[CLEAR] Nenhum deadlock detectado.");
        }
    }


    public void registerProcess(Process process){
        processes.add(process);
    }

    public int getVerificationInterval() {
        return verificationInterval;
    }

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public DisplayScreen getDisplayScreen() {
        return displayScreen;
    }

    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }

    public void waiting(){
        int time = this.verificationInterval;
        LocalTime initial = LocalTime.now();
        while (true) { // função de espera por X segundos
            LocalTime now = LocalTime.now();
            Duration duration = Duration.between(initial, now);
            float length = duration.toMillis() / 1000f;

            if(length >= (float) time){
                break;
            }
        }
    }
    
    @Override
    public void run() {
        while (true) { 
            waiting();
            verify();

        }
    }
}
