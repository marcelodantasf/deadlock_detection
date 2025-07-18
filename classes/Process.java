/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class Process extends Thread{
    
    private List<Resource> resourceList;

    private int id;
    private int intervalRequisition; //em segundos
    private int intervalUsage; //em segundos
    private volatile boolean running = true;

    private Random random = new Random();

    public Process(int id, int intervalRequisition, int intervalUsage, List<Resource> resources) {
        this.id = id;
        this.intervalRequisition = intervalRequisition;
        this.intervalUsage = intervalUsage;
        this.resourceList = resources;
    }

    public int getProcessId() {
        return this.id;
    }

    public void setProcessId(int id) {
        this.id = id;
    }

    public int getIntervalRequisition() {
        return this.intervalRequisition;
    }

    public void setIntervalRequisition(int intervalRequisition) {
        this.intervalRequisition = intervalRequisition;
    }

    public int getIntervalUsage() {
        return this.intervalUsage;
    }

    public void setIntervalUsage(int intervalUsage) {
        this.intervalUsage = intervalUsage;
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

    public void waiting(){
        // função de espera por N segundos
        while (true) { 
            int time = this.intervalRequisition;
            LocalTime initial = LocalTime.now();
            while (true) { 
                LocalTime now = LocalTime.now();
                Duration duration = Duration.between(initial, now);
                float length = duration.toMillis() / 1000f;

                if(length >= (float) time){
                    Resource.currentInstances.release();
                    return;
                }
            }
        }
    }

    @Override
    public void run(){
        while(running){

            waiting();
            try {
                // ResourceConfigScreen.resources
                Resource.currentInstances.acquire();
                System.out.println("Processo " + this.id + " requisitou recurso.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            ProcessThread thread = new ProcessThread(null, this.intervalUsage);
            thread.start();

        }
        System.out.println("Processo interrompido");
    }
}
