import java.util.ArrayList;
import java.util.HashMap;

public class OS {
    private static Kernel kernel;

    private static Scheduler scheduler = new Scheduler(kernel);

    private static CallType current_Call;

    private static ArrayList<Object> parameters = new ArrayList<>();
    private static Object return_Value;

    //hashmap for pid
    private static HashMap<Integer, PCB> pidMap = new HashMap<>();

    //enum storing create and switchprocess
    public enum CallType {
        CreateProcess, SwitchProcess, Sleep, Exit
    }

    //getter for currentcall
    public static CallType getCurrentCall() {
        return current_Call;
    }

    //getter for parameters
    public static ArrayList<Object> getParameters() {
        return parameters;
    }

    //getter for returnvalue
    public static void setReturnValue(Object o) {
        return_Value = o;
    }

    //create process clears parameters, adds the new userland process, sets the calltype to createprocess then switches to kernel.
    //tells the thread to slepe while it waits for returnvalue to stop being null
    public static <Priority> int CreateProcess(UserlandProcess up, PCB.Priority priority) {
        return scheduler.createProcess(up, priority);
    }

    public static int CreateProcess(UserlandProcess up) {
        return scheduler.createProcess(up, PCB.Priority.Interactive);
    }

    //start the userland process
    public static void Startup(UserlandProcess init) {
        kernel = new Kernel();
        CreateProcess(init);

        CreateProcess(new IdleProcess());
    }

    //sets the call to swithcrpocess and calls switchtokernel()
    public static void switchProcess() {
        current_Call = CallType.SwitchProcess;

        switchToKernel();
    }

    //starts kernel then stops the currenrly running in scheduler
    private static void switchToKernel() {
        kernel.start();
        if (kernel.getScheduler().getCurrentlyRunning() != null) {
            kernel.getScheduler().getCurrentlyRunning().stop();
        }
    }

    //new sleep function in OS that calls schedulers sleepProcess function for x ammount of milliseconds
    public static void Sleep(int ms) {
        scheduler.sleepProcess(ms);
    }

    //new Exit function is OS that calls exitCurrentProcess() in scheduler
    public static void Exit() {
        scheduler.exitCurrentProcess();
    }


    // New functions in assignment 4

    //GetPid and GetPidByName call a function in scheduler of the smae name
    public static int GetPid() {
        return scheduler.getPid();
    }


    public static int GetPidByName(String name) {
        return scheduler.getPidByName(name);

    }


    public static void SendMessage(kernelMessage message) {
        message = new kernelMessage(message);
        message.senderPid = GetPid();

        PCB targetPcb = pidMap.get(message.getTargetPid());
        if (targetPcb != null) {
            targetPcb.addMessage(message);


            if (targetPcb.hasMessage()) {
                scheduler.Queue_Distribution_Helper(targetPcb);

            }
        }
    }

    //if it has a message available immediately returns it
    public static kernelMessage WaitForMessage() {
        PCB currentPcb = scheduler.getCurrentlyRunning();


        if (currentPcb.hasMessage()) {
            return currentPcb.getMessage();

        }


        Sleep(0);
        return null;

    }


    //getter for the pidmap
    public static HashMap<Integer, PCB> getPidMap(){
        return pidMap;

    }

}


