public class TestDevices {
    //test file initializes two processes and two devices, one a filesystem and one a random device.
    //It writes to each device and reads from both devices, then exits.
    public static void main(String[] args) {

        UserlandProcess process1 = new TestProcess("Process1");
        UserlandProcess process2 = new TestProcess("Process2");


        OS.Startup(process1);

        OS.CreateProcess(process2, PCB.Priority.Background);


        VFS vfs = new VFS();

        int device1 = vfs.Open("file data.txt");

        int device2 = vfs.Open("random 100");

        if (device1 != -1) {
            System.out.println("Device 1 has opened.");

        } else {
            System.out.println("Device 1 failed to open.");

        }

        if (device2 != -1) {
            System.out.println("Device 2 has opened.");

        } else {
            System.out.println("Device 2 has failed to open.");

        }


        OS.switchProcess();
        byte[] data = "Process 1 test data".getBytes();
        vfs.Write(device1, data);

        byte[] readDevice1 = vfs.Read(device1, 10);
        System.out.println("Read from Device 1: " + new String(readDevice1));


        OS.switchProcess();
        byte[] readDevice2 = vfs.Read(device2, 10);

        System.out.println("Read from Device 2: " + new String(readDevice2));


        vfs.Close(device1);
        vfs.Close(device2);

    }


    // very basic test process to test out the devices functionality
    static class TestProcess extends UserlandProcess {
        private final String processName;

        public TestProcess(String name) {
            this.processName = name;
        }

        @Override
        protected void main() {
            System.out.println(processName + " is running.");
            OS.Sleep(500);
            cooperate();
        }
    }
}
