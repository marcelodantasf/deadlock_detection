
public class ProcessThread extends Thread{

    private Resource resource;
    private int intervalUsage;

    // TODO: adicionar booleano atomico processo e threadprocesso
    public ProcessThread(Resource resource, int intervalUsage) {
        this.resource = resource;
        this.intervalUsage = intervalUsage;
    }
    
    @Override
     public void run(){
        System.out.println("Recurso sendo usado por " + intervalUsage + " segundos");
        int time = intervalUsage;
        for (int i = 1; i <= time; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resource.releaseResource();
        System.out.println("Recurso liberado");
        return;

    }
}
