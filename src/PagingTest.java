public class PagingTest {
    public static void main(String[] args) {
        OS.Startup(new memoryTestA());
        OS.CreateProcess(new memoryTestB());

        // Switch process to run MemoryTestProcessA first, then MemoryTestProcessB
        OS.switchProcess();
    }
}

