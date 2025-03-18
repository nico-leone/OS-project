public class Hardware {
    public static byte[] memory = new byte[1024 * 1024];
    private static final int MEMORY_SIZE = 1024 * 1024;

    //function to read a byte from memory at a vAffress. checks if not in TLB then makes na OS call, finds error is mapd eosnt exist.
    public static byte ReadMemory(UserlandProcess process, int address) {
        int virtualPage = address / 1024; // Page size is 1KB
        int pageOffset = address % 1024;
        int physicalPage = process.getPhysPage(virtualPage);

        if (physicalPage == -1) {
            OS.getMapping(OS.scheduler.getCurrentlyRunning(), virtualPage);
            physicalPage = process.getPhysPage(virtualPage);


            if (physicalPage == -1) {
                System.out.println("invalid address " + address);
                return 0;
            }
        }

        int physicalAddress = (physicalPage * 1024) + pageOffset;
        return memory[physicalAddress];
    }

    // Writes a byte to memory at vAddress
    public static void WriteMemory(UserlandProcess process, int address, byte value) {
        int virtualPage = address / 1024;
        int pageOffset = address % 1024;
        int physicalPage = process.getPhysPage(virtualPage);

        if (physicalPage == -1) {
            OS.getMapping(OS.scheduler.getCurrentlyRunning(), virtualPage);
            physicalPage = process.getPhysPage(virtualPage);

            if (physicalPage == -1) {
                System.out.println("invalid address " + address);
                return;
            }
        }

        int physicalAddress = (physicalPage * 1024) + pageOffset;
        memory[physicalAddress] = value;
    }
}
