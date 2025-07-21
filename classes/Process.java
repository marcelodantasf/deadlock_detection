/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Process extends Thread{
    
    private List<Resource> resourceList;

    private int id;
    private int intervalRequisition; //em segundos
    private int intervalUsage; //em segundos
    private volatile boolean running = true;

    private Map<Integer, Integer> allocated = new HashMap<>();
    private Map<Integer, Integer> requested = new HashMap<>();

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
                    return;
                }
            }
        }
    }

    public int getAllocated(int resourceIndex) {
        return allocated.getOrDefault(resourceIndex, 0);
    }

    public int getRequested(int resourceIndex) {
        return requested.getOrDefault(resourceIndex, 0);
    }

    public void requestResource(){
        Resource resourceSelected = selectResource();
        int index = resourceList.indexOf(resourceSelected);

        System.out.println("Processo " + this.id + " requisitou recurso " + resourceSelected.getName() + " .");
        
        requested.put(index, requested.getOrDefault(index, 0) + 1);
        
        resourceSelected.acquireResource();

        allocated.put(index, allocated.getOrDefault(index, 0) + 1);

        ProcessThread thread = new ProcessThread(resourceSelected, this.intervalUsage);
        thread.start();
    }

    public void mutexAcquire(){
        try{
            ResourceConfigScreen.Mutex.acquire();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void mutexRelease(){
        ResourceConfigScreen.Mutex.release();
    }

    @Override
    public void run(){
        while(running){
            waiting();
            mutexAcquire();
            requestResource();
            mutexRelease();
        }
        System.out.println("Processo interrompido");
    }
}
