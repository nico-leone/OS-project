public class Kernel extends Process implements Device{
    private Scheduler scheduler;

    static boolean[] pageTable = new boolean[1024];
    //constructor initiates the scheduler

    private VFS vfs = new VFS();
    //initiates our VFS
    public Kernel() {
        scheduler = new Scheduler(this);

    }
    //scheduler getter
    public Scheduler getScheduler() {

        return scheduler;
    }
    //main with an infinite loop storing a switch statement that checks whther the current call is createprocess or switch process, then
    //takes actions accoridingly.
    //updated in assignment 4 to account for messages
    @Override
    protected void main() {
        while (true) {
            switch (OS.getCurrentCall()) {
                case CreateProcess://sets the return value to a craeteprocess parameter
                    UserlandProcess up = (UserlandProcess) OS.getParameters().get(0);
                    int pid = scheduler.createProcess(up);

                    OS.getPidMap().put(pid, scheduler.getCurrentlyRunning());
                    OS.setReturnValue(scheduler.createProcess(up));
                    break;
                case SwitchProcess://switches the shedulers process

                    PCB exitingPcb = scheduler.getCurrentlyRunning();
                    if (exitingPcb != null) {
                        OS.getPidMap().remove(exitingPcb.getPid());

                    }
                    scheduler.switchProcess();
                    break;
            }
            if (scheduler.getCurrentlyRunning() != null) {
                scheduler.getCurrentlyRunning().start();

            } else {
                System.out.println("No process left");

            }

            stop();
        }
    }

    public static void sleep(int milliseconds) {
        OS.Sleep(milliseconds);
    }

    public static void createProcess(UserlandProcess up, PCB.Priority priority) {
        OS.CreateProcess(up, priority);
    }

    @Override
    public int Open(String s) {
        PCB current = scheduler.getCurrentlyRunning();
        int id = vfs.Open(s);
        if(id != 1){
            for(int i = 0; i < current.getDeviceIds().length; i++){
                if(current.getDeviceIds()[i] == -1){
                    current.getDeviceIds()[i] = id;

                    return i;

                }

            }
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        PCB current = scheduler.getCurrentlyRunning();
        vfs.Close(current.getDeviceIds()[id]);
        current.getDeviceIds()[id] = -1;

    }

    @Override
    public byte[] Read(int id, int size) {
        PCB current = scheduler.getCurrentlyRunning();
        return vfs.Read(current.getDeviceIds()[id], size);

    }

    @Override
    public void Seek(int id, int to) {
        PCB current = scheduler.getCurrentlyRunning();
        vfs.Seek(current.getDeviceIds()[id], to);

    }

    @Override
    public int Write(int id, byte[] data) {
        PCB current = scheduler.getCurrentlyRunning();
        return vfs.Write(current.getDeviceIds()[id], data);

    }

    //new functions in kernel for assignment 4
    public int GetPid(PCB process) {
        return scheduler.getPid();

    }


    public int GetPidByName(String name) {
        return scheduler.getPidByName(name);

    }

    public VFS getVfs(){
        return vfs;
    }

    //allocatememory and freememory updated for assignment 6.
    public int allocateMemory(int numPages) {
        for (int i = 0; i <= 1024 - numPages; i++) {
            boolean blockAvailable = true;

            // check if the range is free
            for (int j = 0; j < numPages; j++) {
                if (pageTable[i + j]) {
                    blockAvailable = false;
                    break;
                }
            }

            if (blockAvailable) {
                // Mark the found pages as they are used
                for (int j = 0; j < numPages; j++) {
                    pageTable[i + j] = true;
                }
                return i;
            }
        }
        return -1; // Nothing found
    }

    // frees pages at start
    public static void FreeMemory(PCB pcb, int startPage, int pageCount) {
        for (int i = startPage; i < startPage + pageCount; i++) {
            VirtualToPhysicalMapping mapping = pcb.getMapping(i);
            if (mapping != null && mapping.physicalPageNumber != -1) {
                pageTable[mapping.physicalPageNumber] = false;
            }
            pcb.setMapping(i, null);
        }
    }

    // Clears the TLB
    public static void clearTLB() {
        for (int i = 0; i < 2; i++) {
            UserlandProcess.TLB[i][0] = -1;
            UserlandProcess.TLB[i][1] = -1;
        }
    }

    // exits Kernel and makes sure to Free all memory and clear the TLB, updated for assignemnt 6


    public static void Exit(PCB pcb) {
        for (int i = 0; i < 100; i++) {
            VirtualToPhysicalMapping mapping = pcb.getMapping(i);
            if (mapping != null) {
                if (mapping.physicalPageNumber != -1) {
                    pageTable[mapping.physicalPageNumber] = false;
                }
                pcb.setMapping(i, null);
            }
        }
        clearTLB(); // Clear TLB entries
        OS.Exit(); // Exit process
    }
}







