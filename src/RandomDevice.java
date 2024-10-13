import java.util.Random;

public class RandomDevice implements Device{
    private final Random[] randomDevices = new Random[10];

    @Override
    public int Open(String s) {
        for(int i = 0; i < randomDevices.length; i++){
            if(randomDevices[i] == null){
                randomDevices[i] = (s != null && !s.isEmpty()) ? new Random(Integer.parseInt(s)) : new Random();
                return i;
            }
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        if(id <= 0 && id < randomDevices.length){
            randomDevices[id] = null;
        }
    }

    @Override
    public byte[] Read(int id, int size) {
        byte[] readData = new byte[size];
        if(id >=0 && id < randomDevices.length && randomDevices[id] != null){
            randomDevices[id].nextBytes(readData);
        }
        return readData;
    }

    @Override
    public void Seek(int id, int to) {

    }

    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
