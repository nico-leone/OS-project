public class VFS implements Device{
    private final Device[] devices = new Device[10];
    private final int[] ids = new int[10];
    //determines if the device being created will be a File system or random device based on what is inputed
    //as s.
    @Override
    public int Open(String s) {
        String[] deviceData = s.split(" ", 2);
        String name = deviceData[0];

        String param;

        if(deviceData.length > 1){
            param = deviceData[1];

        }
        else{
            param = null;

        }

        Device device;
        if ("file".equalsIgnoreCase(name)) {
            device = new FakeFileSystem();

        }
        else if("random".equalsIgnoreCase(name)){
            device = new RandomDevice();

        }
        else{
            System.out.println("Unknown Device Does Not Exist");

            return -1;

        }

        int d_ID = device.Open(param);
        if(d_ID == -1){
            return -1;

        }

        for(int i = 0; i < devices.length; i++){
            if(devices[i] == null){
                devices[i] = device;
                ids[i] = d_ID;

                return i;

            }
        }
        return -1;
    }

    //closes the device at id
    @Override
    public void Close(int id) {
        if (id >= 0 && id < devices.length && devices[id] != null){
            devices[id].Close(ids[id]);
            devices[id] = null;
            ids[id] = -1;
        }

    }

    @Override
    public byte[] Read(int id, int size) {
        return devices[id].Read(ids[id],size);
    }

    @Override
    public void Seek(int id, int to) {
        devices[id].Seek(ids[id], to);

    }

    @Override
    public int Write(int id, byte[] data) {
        return devices[id].Write(ids[id], data);
    }
}
