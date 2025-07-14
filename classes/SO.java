import java.time.Duration;
import java.time.LocalTime;

public class SO {
    private int deltaT;

    public SO(int deltaT){
        this.deltaT = deltaT;
    }

    public int getDeltaT() {
        return this.deltaT;
    }

    public void waiting(){
        int time = this.deltaT;
        LocalTime initial = LocalTime.now();
        while (true) { // função de espera por X segundos
            LocalTime now = LocalTime.now();
            Duration duration = Duration.between(initial, now);
            float length = duration.toMillis() / 1000f;

            if(length >= (float) time){
                break;
            }
        }
    }

    public void check(){

    }

    public void run(){
        while(true){
            waiting();
            check();
        }
    }
}
