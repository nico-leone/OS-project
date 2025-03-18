//memory test A tests the write memory and read memory functionality.
public class memoryTestA extends UserlandProcess {
    private int allocatedAddress;

    @Override
    protected void main() {
        System.out.println("MemoryTestA starts");

        int PAGE_SIZE = 1024 * 1024;

        allocatedAddress = OS.AllocateMemory(2 * PAGE_SIZE);
        System.out.println("memory at address: " + allocatedAddress);


        int testWriteAddress = allocatedAddress + 100;
        byte testValue = 55;
        Hardware.WriteMemory(this, testWriteAddress, testValue);
        System.out.println("value " + testValue + " at " + testWriteAddress);

        // Test reading from allocated memory
        byte readValue = Hardware.ReadMemory(this, testWriteAddress);
        System.out.println("Read value " + readValue + " from  " + testWriteAddress);

        // Verify if read value matches written value
        if (readValue == testValue) {
            System.out.println("Memory read/write test passed.");
        }


        int extension = allocatedAddress + PAGE_SIZE + 100;
        Hardware.WriteMemory(this, extension, (byte) 99);

        byte extendedReadValue = Hardware.ReadMemory(this, extension);

        System.out.println("Extended memory test read value: " + extendedReadValue);
        if (extendedReadValue == 99) {
            System.out.println("Extended memory write/read test passed.");
        }
    }
}
