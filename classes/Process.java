/*Estes threads deverão solicitar, utilizar e liberar recursos existentes no sistema
Podem existir até 10 processos rodando “simultaneamente”.*/

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

   

    public void executar(){

    }

    @Override
    public void run(){
        while(running){
            //executar
            ProcessThread thread = new ProcessThread();
            thread.run(this.intervalUsage);
            // TODO: temporizador para nova chamada
        }
        System.out.println("Processo interrompido");
    }
}
