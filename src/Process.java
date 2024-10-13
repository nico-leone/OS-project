import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {
    private Thread thread;
    private Semaphore semaphore;

    private boolean quantum_Expired;
    private boolean has_started = false; //boolean to check if the thread is started to prevent an error

    //constructor
    public Process() {
        this.thread = new Thread(this);

        this.semaphore = new Semaphore(0);

        this.quantum_Expired = false;
    }

    //sets expired to stop
    public void requestStop() {
        this.quantum_Expired = true;
    }

    //indicates if semaphore is 0
    public boolean isStopped() {
        return semaphore.availablePermits() == 0;
    }

    //true when the thread is dead
    public boolean isDone() {
        return !thread.isAlive();
    }

    //releases the semaphore and starts the thread
    public void start() {
        if (!has_started) {
            has_started = true;

            thread.start();
        }
        semaphore.release();
    }

    //aquires the semaphore
    public void stop() {
        try {

            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //acquires the semaphore, then calls main
    @Override
    public void run() {
        try {
            semaphore.acquire();

            main();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //sets qE to false if true then calls switchprocess
    public void cooperate() {
        if (quantum_Expired) {

            quantum_Expired = false;

            OS.switchProcess();
        }
    }

    //absract main
    protected abstract void main();
}

