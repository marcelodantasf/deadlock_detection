public class OS extends Thread{
    private int verificationInterval;

    public int getVerificationInterval() {
        return verificationInterval;
    }

    public void setVerificationInterval(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    public OS(int verificationInterval) {
        this.verificationInterval = verificationInterval;
    }

    @Override
    public void run() {
        while (true) { 
            //wait();
            //verify();
        }
    }
}
