//test program for assignmenet 6
public class VirtualMemoryTestProgram extends UserlandProcess {
    @Override
    protected void main() {
        System.out.println(OS.GetPid() + " is running.");


        int startAddress = OS.AllocateMemory(10240); // 10 pages
        if (startAddress == -1) {
            System.out.println("failed to allocate memory");
            return;
        }
        System.out.println("Allocated memory starts here: " + startAddress);


        for (int i = 0; i < 10240; i++) {
            Write(startAddress + i, (byte) (i % 256));
        }
        System.out.println("Data written to memory");


        boolean success = true;
        for (int i = 0; i < 10240; i++) {
            if (Read(startAddress + i) != (byte) (i % 256)) {
                success = false;
                break;
            }
        }
        if (success) {
            System.out.println("Memory read/write was successful");
        } else {
            System.out.println("Memory read/write has failed");
        }


        OS.Exit();
    }
}
