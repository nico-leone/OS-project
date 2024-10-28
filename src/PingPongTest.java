public class PingPongTest {
    public static void main(String[] args) {
        // starts and creates our process
        OS.Startup(new Ping());
        OS.CreateProcess(new Pong());

        // switches process to begin
        OS.switchProcess();
    }
}
