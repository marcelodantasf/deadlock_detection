import java.time.Duration;
import java.time.LocalTime;

public class ProcessThread extends Thread{
     public void run(int intervalUsage){
        
        while (true) { 
            int time = intervalUsage;
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
}
