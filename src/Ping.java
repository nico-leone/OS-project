
//class to create the ping test function
public class Ping extends UserlandProcess {
    private int pongPid;

    @Override
    protected void main() {

        pongPid = OS.GetPidByName("Pong");

        System.out.println("I am PING, pong = " + pongPid);


        sendMessageToPong(0);

        //loop to listen and respond to pong
        while (true) {
            kernelMessage message = OS.WaitForMessage();
            assert message != null;

            System.out.printf("PING: from: %d to: %d what: %d\n",
                    message.getSenderPid(), message.getTargetPid(), message.getWhat());

            //updates the pong value
            sendMessageToPong(message.getWhat() + 1);
        }
    }


    //helper function to send a message to the pong
    private void sendMessageToPong(int what) {
        kernelMessage msg = new kernelMessage(OS.GetPid(), pongPid, what, new byte[0]);
        OS.SendMessage(msg);

    }
}
