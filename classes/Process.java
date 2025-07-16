/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

import java.time.Duration;
import java.time.LocalTime;

public class Process extends Thread{
    private int id;
    private int intervalRequisition; //em segundos
    private int intervalUsage; //em segundos
    private volatile boolean running = true;

    public Process(int id, int intervalRequisition, int intervalUsage) {
        this.id = id;
        this.intervalRequisition = intervalRequisition;
        this.intervalUsage = intervalUsage;
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

    public void getResource(){
        try {
            Resource.currentInstances.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) { 
            int time = this.intervalUsage;
            LocalTime initial = LocalTime.now();
            while (true) { // função de espera por X segundos
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

    public void executar(){

    }

    @Override
    public void run(){
        while(running){
            //executar
        }
        System.out.println("Processo interrompido");
    }
}
