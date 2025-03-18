//memory test B tests accessing unauthorized functionality.

public class memoryTestB extends UserlandProcess{

    @Override
    protected void main() {
        System.out.println("Starting MemoryTestProcessB");


        int PAGE_SIZE = 1024 * 1024;
        try {
            int unauthorizedAddress = 3 * PAGE_SIZE;
            byte value = Hardware.ReadMemory(this, unauthorizedAddress);

            System.out.println("Unauthorized memory access succeeded unexpectedly. Value: " + value);
        } catch (Exception e) {
            System.out.println("Unauthorized memory access correctly prevented: " + e.getMessage());
        }
    }
}
