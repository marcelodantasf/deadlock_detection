import java.util.List;

public class OS extends Thread{
    private final int verificationInterval;
    private DisplayScreen displayScreen;

    private List<Process> processList;

    private int[] existentResources;
    private int[] availableResources;
    private int[][] currentAlocation;
    private int[][] requisitions;

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public DisplayScreen getDisplayScreen() {
        return displayScreen;
    }

    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }

    public int getVerificationInterval() {
        return verificationInterval;
    }

    public void setProcesses(List<Process> processList) {
        this.processList = processList;
    }

    public void setExistentResources(int[] existentResources) {
        this.existentResources = existentResources;
    }
    
    public void waiting(){
        try {
            sleep(1000 * this.verificationInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateMatricesFromProcesses() {
        if (processList == null || processList.isEmpty()) return;

        int n = processList.size();
        int m = existentResources.length;

        currentAlocation = new int[n][m];
        requisitions = new int[n][m];
        availableResources = new int[m];

        // Atualiza C e R com base nos processos
        for (int i = 0; i < n; i++) {
            Process p = processList.get(i);
            for (int j = 0; j < m; j++) {
                currentAlocation[i][j] = p.getAllocated(j);
                requisitions[i][j] = p.getRequested(j);
            }
        }

        // Calcula recursos disponíveis: A = E - soma coluna(C)
        for (int j = 0; j < m; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += currentAlocation[i][j];
            }
            availableResources[j] = existentResources[j] - sum;
        }
    }
    
    private void verify() {
        int n = currentAlocation.length;
        int m = currentAlocation[0].length;

        boolean[] finished = new boolean[n];
        int[] work = availableResources.clone();

        boolean progress;

        do {
            progress = false;

            for (int i = 0; i < n; i++) {
                if (!finished[i]) {
                    boolean canExecute = true;

                    for (int j = 0; j < m; j++) {
                        if (requisitions[i][j] > work[j]) {
                            canExecute = false;
                            break;
                        }
                    }

                    if (canExecute) {
                        for (int j = 0; j < m; j++) {
                            work[j] += currentAlocation[i][j];
                        }
                        finished[i] = true;
                        progress = true;
                        System.out.println("[OS] Processo P" + i + " finalizado (simulado).");
                    }
                }
            }
        } while (progress);

        boolean deadlockDetected = false;
        for (int i = 0; i < n; i++) {
            if (!finished[i]) {
                deadlockDetected = true;
                System.out.println("⚠ Deadlock detectado com processo P" + i);
            }
        }

        if (!deadlockDetected) {
            System.out.println("✅ Nenhum deadlock detectado.");
        }
    }

    @Override
    public void run() {
        while (true) { 
            waiting();
            updateMatricesFromProcesses();
            verify();
        }
    }
}