import java.time.Duration;
import java.time.LocalTime;

public class OS extends Thread{
    private int verificationInterval;

    public int getVerificationInterval() {
        return verificationInterval;
    }

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public void waiting(){
        int time = this.verificationInterval;
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
    
    @Override
    public void run() {
        while (true) { 
            waiting();
            //verify();

        }
    }
}
