import java.util.Arrays;
import java.util.LinkedList;

public class PCB {
    private static int nextPid = 0;
    private int pid;

    private UserlandProcess ulp;
    private Priority priority;
    private long wakeTime = 0;

    private int[] d_IDs = new int[10];

    //new members for assignment 4
    private LinkedList<kernelMessage> messageQueue = new LinkedList<>(); // Message queue
    private String name; // Process name
    // replacement for pageArray in assignment 6, memoryMap now stores an array of 100 virtualtophysicalmappings
    private VirtualToPhysicalMapping[] memoryMap = new VirtualToPhysicalMapping[100];

    public enum Priority {
        RealTime, Interactive, Background
    }


    public PCB(UserlandProcess up, Priority priority) {
        this.pid = nextPid++;
        this.ulp = up;

        this.priority = priority;
        this.name = ulp.getClass().getSimpleName();

        Arrays.fill(d_IDs, -1);

        Arrays.fill(memoryMap, null);

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


    //new functions for assignment 4

    public void addMessage(kernelMessage message) {
        messageQueue.add(message);

    }

    public kernelMessage getMessage() {
        return messageQueue.poll(); // Retrieve and remove the head of the message queue

    }

    public boolean hasMessage() {
        return !messageQueue.isEmpty(); // Check if the queue is not empty

    }

    public String getName() {
        return name;

    }

    public VirtualToPhysicalMapping getMapping(int index) {
        return memoryMap[index];
    }

    public void setMapping(int index, VirtualToPhysicalMapping mapping) {
        memoryMap[index] = mapping;
    }







}





