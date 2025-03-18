//test file for assignment 6
public class VMtest {
    public static void main(String[] args) {
        OS.Startup(new VirtualMemoryTestProgram());

        // Multiple processes created for swapping and allocation testing
        for (int i = 0; i < 5; i++) {
            OS.CreateProcess(new VirtualMemoryTestProgram());
        }

        // trigger process swapping
        for (int i = 0; i < 5; i++) {
            OS.switchProcess();
        }
    }
}
