import java.util.concurrent.Semaphore;

public class Resource {
    public Semaphore currentInstances;

    private final String name;
    private final int id;
    private int maxInstances;
    
    public Resource(String name, int id, int maxInstances) {
        this.name = name;
        this.id = id;
        this.maxInstances = maxInstances;
        this.currentInstances = new Semaphore(maxInstances, true);
    }

    public void acquireResource() {
        try {
            currentInstances.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public void releaseResource(){
        currentInstances.release();
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getMaxInstances() {
        return this.maxInstances;
    }

    @Override
    public String toString() {
        return "Recurso [name=" + name + ", id=" + id + ", instances=" + (maxInstances - currentInstances.availablePermits()) +  ", maxInstances=" + maxInstances + "]";
    }

    /*public void setCurrentInstances(int currentInstances) {
        this.currentInstances = currentInstances;
    }*/

}
