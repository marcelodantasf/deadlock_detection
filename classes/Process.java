import java.util.Queue;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;

public class Process extends Thread {

    private List<Resource> resourceList;
    private Queue<Resource> resourcesRequested = new LinkedList<>();
    private Queue<Resource> resourcesBeingUsed = new LinkedList<>();

    private int id;
    private int intervalRequisition; // em segundos
    private int intervalUsage;       // em segundos
    private volatile boolean running = true;
    private DisplayScreen displayScreen;

    private Random random = new Random();

    public Process(int id, int intervalRequisition, int intervalUsage, List<Resource> resources) {
        this.id = id;
        this.intervalRequisition = intervalRequisition;
        this.intervalUsage = intervalUsage;
        this.resourceList = resources;
    }

    public DisplayScreen getDisplayScreen() {
        return displayScreen;
    }

    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }

    public int getProcessId() {
        return this.id;
    }

    public void setProcessId(int id) {
        this.id = id;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void restart() {
        this.running = true;
    }

    public void kill(){
        this.running = false;
    }

    private Resource selectResource() {
        if (resourceList == null || resourceList.isEmpty())
            return null;
        return resourceList.get(random.nextInt(resourceList.size()));
    }

    public void waitASec(){
        // função de espera por 1 segundo
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getRequested(int resourceIndex) {
        int count = 0;
        for (Resource r : resourcesRequested) {
            if (resourceList.indexOf(r) == resourceIndex) count++;
        }
        return count;
    }

    public int getAllocated(int resourceIndex) {
        int count = 0;
        for (Resource r : resourcesBeingUsed) {
            if (resourceList.indexOf(r) == resourceIndex) count++;
        }
        return count;
    }

    public void requestResource() {
        Resource resourceSelected = selectResource();
        if (resourceSelected == null) return;

        resourcesRequested.add(resourceSelected);
        System.out.println("Processo " + this.id + " requisitou recurso " + resourceSelected.getName());

        ResourceConfigScreen.mutexAcquire();
        resourceSelected.acquireResource();
        ResourceConfigScreen.mutexRelease();

        resourcesBeingUsed.add(resourceSelected);
    }

    public void releaseNextResource() {
        if (!resourcesBeingUsed.isEmpty()) {
            Resource resource = resourcesBeingUsed.poll();
            ResourceConfigScreen.mutexAcquire();
            resource.releaseResource();
            ResourceConfigScreen.mutexRelease();
            System.out.println("Processo " + this.id + " liberou recurso " + resource.getName());
        }
    }

    @Override
    public void run() {
        int deltR = this.intervalRequisition;
        int deltU = this.intervalUsage;

        int t = 0;

        while (running) {
            waitASec();
            t++;

            if (t % deltR == 0) {
                requestResource();
            }

            if (t % deltU == 0) {
                releaseNextResource();
            }
        }

        String killMsg = "Processo " + this.id + " interrompido";
        System.out.println(killMsg);
        if (displayScreen != null) {
            displayScreen.log(killMsg);
        }
    }
}