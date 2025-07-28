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

    public void requestResource(){
        Resource resourceSelected = selectResource();
        String msg = "Processo " + this.id + " requisitou recurso " + resourceSelected.getName() + " .";
        System.out.println(msg);
        displayScreen.log(msg);
        
        resourceSelected.acquireResource();
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
        String killMsg = "Processo " + this.id + " interrompido";
        System.out.println(killMsg);
        displayScreen.log(killMsg);
    }
}
