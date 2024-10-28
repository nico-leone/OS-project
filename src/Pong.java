//class to create the pong test function

public class Pong extends UserlandProcess {
    private int pingPid;

    @Override
    protected void main() {

        pingPid = OS.GetPidByName("Ping");
        System.out.println("I am PONG, ping = " + pingPid);

        // loop to listen and respond to ping
        while (true) {
            kernelMessage message = OS.WaitForMessage();
            assert message != null;
            System.out.printf("PONG: from: %d to: %d what: %d\n",
                    message.getSenderPid(), message.getTargetPid(), message.getWhat());

            // updates what value
            sendMessageToPing(message.getWhat() + 1);
        }
    }

    //helper function to send message to ping
    private void sendMessageToPing(int what) {
        kernelMessage msg = new kernelMessage(OS.GetPid(), pingPid, what, new byte[0]);
        OS.SendMessage(msg);

    }
}
