import java.util.Queue;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;

public class Process extends Thread {

    private List<Resource> resourceList;
    private List<Resource> resourcesRequested = new LinkedList<>();
    private List<Resource> resourcesBeingUsed = new LinkedList<>();

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

    public String getResourceBeingUsedList() {
        String str = "";
        for(int i = 0; i<resourcesBeingUsed.size(); i++) {
            str +=  resourcesBeingUsed.get(i).getName() + ";\n";
        }
        return str;
    }

    public String getResourceRequested() {
        String str = "";
        for(int i = 0; i<resourcesRequested.size(); i++) {
            str += resourcesRequested.get(i).getName() + ";\n";
        }
        return str;
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
        if(running) {
            for (int i = resourcesBeingUsed.size() - 1; i>=0 ; i--){
                Resource r = resourcesBeingUsed.get(i);
                r.releaseResource();
                resourcesBeingUsed.remove(r);
            }
            this.running = false;
            this.interrupt();
        }
        return;
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
            Thread.currentThread().interrupt();
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
        String msg = ("Processo " + this.id + " requisitou recurso " + resourceSelected.getName());
        displayScreen.log(msg);
        System.out.println(msg);

        ResourceConfigScreen.mutexAcquire();
        resourceSelected.acquireResource();
        ResourceConfigScreen.mutexRelease();

        resourcesBeingUsed.add(resourceSelected);
        resourcesRequested.removeFirst();
    }

    public void releaseNextResource() {
        if (!resourcesBeingUsed.isEmpty()) {
            Resource resource = resourcesBeingUsed.removeFirst();
            ResourceConfigScreen.mutexAcquire();
            resource.releaseResource();
            ResourceConfigScreen.mutexRelease();
            String msg = ("Processo " + this.id + " liberou recurso " + resource.getName());
            displayScreen.log(msg);
            System.out.println(msg);
        }
    }

    @Override
    public void run() {
        int deltR = this.intervalRequisition;
        int deltU = this.intervalUsage;

        int t = 0;

        while (running) {
            waitASec();
            if(!running) break;
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