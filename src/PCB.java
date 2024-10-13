import java.util.Arrays;

public class PCB {
    private static int nextPid = 0;
    private int pid;

    private UserlandProcess ulp;
    private Priority priority;
    private long wakeTime = 0;

    private int[] d_IDs = new int[10];


    public enum Priority {
        RealTime, Interactive, Background
    }


    public PCB(UserlandProcess up, Priority priority) {
        this.pid = nextPid++;
        this.ulp = up;

        this.priority = priority;

        Arrays.fill(d_IDs, -1);

    }


    public void stop() {
        ulp.stop();

        while (!ulp.isStopped()) {
            try {
                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }



    public boolean isDone() {
        return ulp.isDone();
    }

    public void start() {
        ulp.start();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getPid() {
        return pid;
    }

    public UserlandProcess getProcess() {
        return ulp;
    }

    public void setWakeTime(long wakeTime) {
        this.wakeTime = wakeTime;
    }

    public long getWakeTime() {
        return wakeTime;
    }

    //getter for device IDs
    public int[] getDeviceIds() {
        return d_IDs;
    }


}


