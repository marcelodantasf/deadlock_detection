import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OS extends Thread {
    private final int verificationInterval;
    private DisplayScreen displayScreen;

    private final List<Process> processList = new CopyOnWriteArrayList<>(); 
    private final List<Resource> resourceList = new CopyOnWriteArrayList<>(); 

    private int[] existentResources;  
    private int[] availableResources;
    private int[][] currentAlocation;
    private int[][] requisitions;

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public void addResource(Resource resource) {
        resourceList.add(resource);
        updateExistentResources(); 
    }

    public void addProcess(Process process) {
        processList.add(process);
    }

    private void updateExistentResources() {
        existentResources = new int[resourceList.size()];
        for (int i = 0; i < resourceList.size(); i++) {
            existentResources[i] = resourceList.get(i).getMaxInstances();
        }
    }

    public void updateMatrices() {
        int n = processList.size();
        int m = resourceList.size();

        currentAlocation = new int[n][m];
        requisitions = new int[n][m];
        availableResources = new int[m];

        for (int i = 0; i < n; i++) {
            Process p = processList.get(i);
            for (int j = 0; j < m; j++) {
                currentAlocation[i][j] = p.getAllocated(j); 
            }
        }

        for (int i = 0; i < n; i++) {
            Process p = processList.get(i);
            for (int j = 0; j < m; j++) {
                requisitions[i][j] = p.getRequested(j); 
            }
        }

        for (int j = 0; j < m; j++) {
            int sumAllocated = 0;
            for (int i = 0; i < n; i++) {
                sumAllocated += currentAlocation[i][j];
            }
            availableResources[j] = existentResources[j] - sumAllocated;
        }
    }

    private void verifyDeadlock() {
        updateMatrices();

        int n = processList.size();
        int m = resourceList.size();

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
                    }
                }
            }
        } while (progress);

        StringBuilder deadlockInfo = new StringBuilder();
        boolean deadlockDetected = false;

        for (int i = 0; i < n; i++) {
            if (!finished[i]) {
                deadlockDetected = true;
                deadlockInfo.append("\nProcesso P").append(processList.get(i).getProcessId())
                    .append(" está em deadlock.\nRecursos alocados:\n");

                for (int j = 0; j < m; j++) {
                    if (currentAlocation[i][j] > 0) {
                        deadlockInfo.append("- ").append(resourceList.get(j).getName())
                            .append(" (ID: ").append(resourceList.get(j).getId()).append(")\n");
                    }
                }

                deadlockInfo.append("Recursos requisitados:\n");
                for (int j = 0; j < m; j++) {
                    if (requisitions[i][j] > 0) {
                        deadlockInfo.append("- ").append(resourceList.get(j).getName())
                            .append(" (ID: ").append(resourceList.get(j).getId()).append(")\n");
                    }
                }
            }
        }

        if (deadlockDetected) {
            displayScreen.log("\n[SO] DEADLOCK DETECTADO");
            displayScreen.log(deadlockInfo.toString());
        } else {
            displayScreen.log("[SO] Nenhum deadlock detectado.");
        }
    }

    @Override
    public void run() {
        displayScreen.log("[SO] INICIADO! TEMPO DE CHECAGEM: " + this.verificationInterval);
        while (true) {
            try {
                Thread.sleep(verificationInterval * 1000);
                verifyDeadlock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Getters e setters
    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }
}