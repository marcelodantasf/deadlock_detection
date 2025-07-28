/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.LinkedList;

public class Process extends Thread{
    
    private List<Resource> resourceList;
    private Queue<Resource> resourceBeingUsed = new LinkedList<>();

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

    public void waitASec(){
        // função de espera por N segundos
        try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        if (resourceSelected == null) return;

        int index = resourceList.indexOf(resourceSelected);

        System.out.println("Processo " + this.id + " requisitou recurso " + resourceSelected.getName() + ".");

        requested.put(index, requested.getOrDefault(index, 0) + 1);

        ResourceConfigScreen.mutexAcquire();
        resourceSelected.acquireResource();
        ResourceConfigScreen.mutexRelease();

        allocated.put(index, allocated.getOrDefault(index, 0) + 1);
        resourceBeingUsed.add(resourceSelected);
    }

    public void releaseResource(int resourceIndex) {
        if (allocated.containsKey(resourceIndex)) {
            ResourceConfigScreen.mutexAcquire();
            resourceList.get(resourceIndex).releaseResource();
            ResourceConfigScreen.mutexRelease();
            int current = allocated.get(resourceIndex);
            if (current > 1) {
                allocated.put(resourceIndex, current - 1);
            } else {
                allocated.remove(resourceIndex);
            }
            System.out.println("Processo " + this.id + " liberou recurso " + resourceList.get(resourceIndex).getName());
        }
    }

    public void releaseNextResource() {
        if (!resourceBeingUsed.isEmpty()) {
            Resource resource = resourceBeingUsed.poll();
            int index = resourceList.indexOf(resource);
            releaseResource(index);
            return;
        }
        return;
    }

    @Override
    public void run(){
        int deltR = this.intervalRequisition;
        int deltU = this.intervalUsage;

        int t = 0;
        int requests = 0;

        while(running){

            waitASec();
            t++;

            if (t % deltR == 0){
                requests++;
                // TODO: mudar método de aquisição
                requestResource();
            }

            if(t - (requests * deltU) == intervalUsage){
                releaseNextResource();
                // TODO: implementar lógica de liberar o recurso
            }
            
        }
        System.out.println("Processo interrompido");
    }
}
