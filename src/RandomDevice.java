import java.util.Random;

public class RandomDevice implements Device{
    private final Random[] randomDevices = new Random[10];
    //array of random values

    //open checks for an empty value in the randomDevices array, then either adds a random value or a random value with a given seed
    //returns the id
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

    //closes the device at the given id
    @Override
    public void Close(int id) {
        if(id <= 0 && id < randomDevices.length){
            randomDevices[id] = null;
        }
    }

    //reads the value of the device at the given id
    //returns the data
    @Override
    public byte[] Read(int id, int size) {
        byte[] readData = new byte[size];
        if(id >=0 && id < randomDevices.length && randomDevices[id] != null){
            randomDevices[id].nextBytes(readData);
        }
        return readData;
    }

    //empty, does nothing in a random array
    @Override
    public void Seek(int id, int to) {

    }

    //returns 0
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
