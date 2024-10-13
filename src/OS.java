import java.util.ArrayList;

public class OS {
    private static Kernel kernel;

    private static Scheduler scheduler = new Scheduler();

    private static CallType current_Call;

    private static ArrayList<Object> parameters = new ArrayList<>();
    private static Object return_Value;

    //enum storing craete and switchprocess
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
}


