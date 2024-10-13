public class Kernel extends Process {
    private Scheduler scheduler;
    //constructor initiates the scheduler
    public Kernel() {
        scheduler = new Scheduler();

    }
    //scheduler getter
    public Scheduler getScheduler() {

        return scheduler;
    }
    //main with an infinite loop storing a switch statement that checks whther the current call is createprocess or switch process, then
    //takes actions accoridingly.
    @Override
    protected void main() {
        while (true) {
            switch (OS.getCurrentCall()) {
                case CreateProcess://sets the return value to a craeteprocess parameter
                    UserlandProcess up = (UserlandProcess) OS.getParameters().get(0);

                    OS.setReturnValue(scheduler.createProcess(up));
                    break;
                case SwitchProcess://switches the shedulers process
                    scheduler.switchProcess();
                    break;
            }
            scheduler.getCurrentlyRunning().start();
            stop();
        }
    }

    public static void sleep(int milliseconds) {
        OS.Sleep(milliseconds);
    }

    public static void createProcess(UserlandProcess up, PCB.Priority priority) {
        OS.CreateProcess(up, priority);
    }

    public static void exit() {
        OS.Exit();
    }
}


