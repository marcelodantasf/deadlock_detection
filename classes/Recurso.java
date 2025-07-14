import java.util.concurrent.Semaphore;

public class Recurso {
    public static Semaphore currentInstances;

    private String name;
    private int id;
    private int maxInstances;
    //private int currentInstances;
    
    public Recurso(String name, int id, int maxInstances) {
        this.name = name;
        this.id = id;
        this.maxInstances = maxInstances;
        //this.currentInstances = maxInstances;
        Recurso.currentInstances = new Semaphore(maxInstances);
    }
    
    /*public int getCurrentInstances() {
        return currentInstances;
    }*/

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getMaxInstances() {
        return this.maxInstances;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
    }

    @Override
    public String toString() {
        return "Recurso [name=" + name + ", id=" + id + ", instances=" + (maxInstances - currentInstances.availablePermits()) +  ", maxInstances=" + maxInstances + "]";
    }

    /*public void setCurrentInstances(int currentInstances) {
        this.currentInstances = currentInstances;
    }*/

}
