public class kernelMessage {
    int senderPid;
    private int targetPid;
    private int what;
    private byte[] data;

    public kernelMessage(int senderPid, int targetPid, int messageType, byte[] data) {
        this.senderPid = senderPid;
        this.targetPid = targetPid;

        this.what = messageType;
        this.data = data;

    }

    //copy constructor
    public kernelMessage(kernelMessage km) {
        this.senderPid = km.senderPid;
        this.targetPid = km.targetPid;

        this.what = km.what;
        this.data = km.data.clone();

    }

    // Getters for out members
    public int getSenderPid() {
        return senderPid;

    }

    public int getTargetPid() {
        return targetPid;

    }

    public int getMessageType() {
        return what;

    }

    public byte[] getData() {
        return data;

    }

    public int getWhat(){
        return what;

    }



    //simple tostring
    @Override
    public String toString() {
        return "KernelMessage{" +
                "senderPid=" + senderPid +
                ", targetPid=" + targetPid +
                ", messageType=" + what +
                ", data=" + new String(data) + // For debugging, converting byte array to String
                '}';
    }
}
