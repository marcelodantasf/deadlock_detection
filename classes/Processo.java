/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

public class Processo {
    private int id;
    private int intervalRequisition; //segundos
    private int intervalUsage; //em segundos
    private volatile boolean running = true;

    public Processo(int id, int intervalRequisition, int intervalUsage) {
        this.id = id;
        this.intervalRequisition = intervalRequisition;
        this.intervalUsage = intervalUsage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntervalRequisition() {
        return intervalRequisition;
    }

    public void setIntervalRequisition(int intervalRequisition) {
        this.intervalRequisition = intervalRequisition;
    }

    public int getIntervalUsage() {
        return intervalUsage;
    }

    public void setIntervalUsage(int intervalUsage) {
        this.intervalUsage = intervalUsage;
    }

    public boolean isRunning() {
        return running;
    }

    public void restart() {
        this.running = true;
    }

    public void kill(){
        this.running = false;
    }

    public void run(){
        while(running){
            //executar
        }
        System.out.println("Processo interrompido");
    }
}
