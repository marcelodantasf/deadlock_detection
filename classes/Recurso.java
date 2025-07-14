import java.util.concurrent.Semaphore;

public class Resource {
    public static Semaphore currentInstances;

    private String name;
    private int id;
    private int maxInstances;
    //private int currentInstances;
    
    public Resource(String name, int id, int maxInstances) {
        this.name = name;
        this.id = id;
        this.maxInstances = maxInstances;
        //this.currentInstances = maxInstances;
        Resource.currentInstances = new Semaphore(maxInstances);
    }
    
    /*public int getCurrentInstances() {
        return currentInstances;
    }*/

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMaxInstances() {
        return maxInstances;
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
        return "Recurso [name=" + name + ", id=" + id + ", maxInstances=" + maxInstances + "]";
    }

    /*public void setCurrentInstances(int currentInstances) {
        this.currentInstances = currentInstances;
    }*/

}
