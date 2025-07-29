import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    public void kill() {
        if (running) {
            running = false;
            this.interrupt(); // Interrupt any waiting operations
            
            // Release all resources without holding mutex the entire time
            while (!resourcesBeingUsed.isEmpty()) {
                Resource resource = resourcesBeingUsed.get(0);
                try {
                    ResourceConfigScreen.mutexAcquire();
                    resource.releaseResource();
                    resourcesBeingUsed.remove(resource);
                    ResourceConfigScreen.mutexRelease();
                    
                    String msg = "Processo " + this.id + " liberou recurso " + resource.getName() + " durante término";
                    displayScreen.log(msg);
                    System.out.println(msg);
                } catch (Exception e) {
                    ResourceConfigScreen.mutexRelease();
                    break;
                }
            }
            resourcesBeingUsed.removeAll(resourceList);
            resourcesRequested.removeAll(resourceList);
        }
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

        try {
            resourceSelected.Mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resourceSelected.acquireResource();
        resourceSelected.Mutex.release();

        resourcesBeingUsed.add(resourceSelected);
        resourcesRequested.remove(resourceSelected);
    }

    public void releaseNextResource() {
        if (!resourcesBeingUsed.isEmpty()) {
            Resource resource = ((LinkedList<Resource>) resourcesBeingUsed).removeFirst();
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
        int tempoRequisicao = this.intervalRequisition;
        int tempoUso = this.intervalUsage;

        int tempo = 0;

        while (running) {
            waitASec();
            if(!running) break;
            tempo++;

            if (tempo % tempoRequisicao == 0) {
                requestResource();
            }

            if (tempo > tempoUso && (tempo - tempoUso) % tempoRequisicao == 0) {
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