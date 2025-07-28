
public class OS extends Thread{
    private final int verificationInterval;
    private DisplayScreen displayScreen;

    private int[] existentResources;
    private int[] availableResources;
    private int[][] currentAlocation;
    private int[][] requisitions;

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public int getVerificationInterval() {
        return verificationInterval;
    }

    public DisplayScreen getDisplayScreen() {
        return displayScreen;
    }

    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }

    public void waiting(){
        try {
            sleep(1000 * this.verificationInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void verify() {
        int n = requisitions.length;  // número de processos
        int m = requisitions[0].length; // número de recursos

        for (int j = 0; j < m; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += currentAlocation[i][j];
            }
            this.availableResources[j] = this.existentResources[j] - sum;
        }

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
                        // Simula a liberação de recursos
                        for (int j = 0; j < m; j++) {
                            work[j] += currentAlocation[i][j];
                        }
                        finished[i] = true;
                        progress = true;
                    }
                }
            }

        } while (progress);

        // Verifica os processos que não conseguiram terminar
        boolean deadlockDetected = false;
        for (int i = 0; i < n; i++) {
            if (!finished[i]) {
                deadlockDetected = true;
                System.out.println("Deadlock detectado no processo P" + i);
            }
        }

        if (!deadlockDetected) {
            System.out.println("Nenhum deadlock detectado.");
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
