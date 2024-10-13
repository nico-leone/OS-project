import java.time.Clock;
import java.util.*;

public class Scheduler {
    private final Queue<PCB> realtime_processes = new LinkedList<>();
    private final Queue<PCB> interactive_processes = new LinkedList<>();
    private final Queue<PCB> background_processes = new LinkedList<>();

    private final List<PCB> sleeping_processes = new ArrayList<>();

    private PCB currently_running;

    private Clock clock = Clock.systemDefaultZone();
    private Random random = new Random();

    //reference to kernel
    private final Kernel kernel;

    public Scheduler(Kernel kernel){
        this.kernel = kernel;
    }

    //new createprocess function that accepts a priority as input as well as a userland object
    //creates a PCB out of that object and priority and adds it to the processqueue using the helper function
    //if nothing is currently running calls switchprocess, then returns the Pid
    public int createProcess(UserlandProcess up, PCB.Priority priority) {

        PCB pcb = new PCB(up, priority);
        Queue_Distribution_Helper(pcb);

        if (currently_running == null) {
            switchProcess();

        }
        return pcb.getPid();
    }
    //create process function that takes in no priority and defaults it to interactive
    public int createProcess(UserlandProcess up) {

        PCB pcb = new PCB(up, PCB.Priority.Interactive);
        Queue_Distribution_Helper(pcb);

        if (currently_running == null) {
            switchProcess();

        }
        return pcb.getPid();
    }
    //helper function that adds a given PCB process to the correct queue
    private void Queue_Distribution_Helper(PCB pcb) {

        switch (pcb.getPriority()) {
            case RealTime:
                realtime_processes.offer(pcb);
                break;

            case Interactive:
                interactive_processes.offer(pcb);
                break;

            case Background:
                background_processes.offer(pcb);
                break;
        }
    }

    //swithc process function first wakes up sleeping process, gets the next process, then sends it to the
    //helper function to be added to a queue
    public void switchProcess() {
        wakeUpSleepingProcesses();

        PCB next = selectNextProcess();
        if (currently_running != null) {

            currently_running.stop();
            Queue_Distribution_Helper(currently_running);

            if(currently_running.isDone()){
                closeDevices(currently_running);
            }

        }

        if (next != null) {

            next.start();
            currently_running = next;

        }
    }

    //additional helper function to randomly select the next process to be run
    //first checks if there is a realtime process available, then selects either a realtime process, interactive process, or background process, based of a chance
    //with the 60% chance of selecting a realtime process, 30% of selecting interactive, and 10% of selecting background
    //if realtime process ends up being empty, it will then look to interactive processes, and if that queue is empty it will default to background processes
    private PCB selectNextProcess() {
        if (!realtime_processes.isEmpty()) {

            int rand = random.nextInt(10);

            if (rand < 6) {
                return realtime_processes.poll();

            } else if (!interactive_processes.isEmpty() && rand < 9) {
                return interactive_processes.poll();

            } else if (!background_processes.isEmpty()) {
                return background_processes.poll();

            }
        } else if (!interactive_processes.isEmpty()) {
            return random.nextInt(4) < 3 ? interactive_processes.poll() : background_processes.poll();

        } else {
            return background_processes.poll();

        }
        return null;
    }

    //function to wake up any sleeping processes that are in the sleeping processes array based off their waketime, or if the amount of time they were supposed to be slepeing
    //is up
    private void wakeUpSleepingProcesses() {
        long time = clock.millis();
        List<PCB> wakingup = new ArrayList<>();

        for (PCB pcb : sleeping_processes) {

            if (pcb.getWakeTime() <= time) {
                wakingup.add(pcb);

            }
        }
        sleeping_processes.removeAll(wakingup);

        for (PCB pcb : wakingup) {
            Queue_Distribution_Helper(pcb);

        }
    }

    //new function to sleep a process for ms amount of milliseconds
    public void sleepProcess(int ms) {
        if (currently_running != null) {
            long time = clock.millis() + ms;

            currently_running.setWakeTime(time);

            sleeping_processes.add(currently_running);

            currently_running = null;

            switchProcess();
        }
    }

    //function to demote a process
    public void demoteProcess(PCB pcb) {

        if (pcb.getPriority() == PCB.Priority.RealTime) {
            pcb.setPriority(PCB.Priority.Interactive);
            interactive_processes.add(pcb);

        } else if (pcb.getPriority() == PCB.Priority.Interactive) {
            pcb.setPriority(PCB.Priority.Background);
            background_processes.add(pcb);

        }
    }

    public PCB getCurrentlyRunning() {
        return currently_running;
    }

    public void exitCurrentProcess() {
        currently_running = null;
        switchProcess();

    }

    //closes all opened devices
    public void closeDevices(PCB pcb){
        for(int i : pcb.getDeviceIds()) {
            if (i != -1){
                kernel.Close(i);
            }

        }

    }



}

