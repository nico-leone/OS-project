import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device{
    private final RandomAccessFile[] randomAccessFiles= new RandomAccessFile[10];

    //finds an empty slot in the randomAccessFiles array and adds a new file with given value
    //returns id
    @Override
    public int Open(String s) {
        if(s == null || s.isEmpty()){
            throw new IllegalArgumentException("Filename is null or empty");

        }
        try {
            for (int i = 0; i < randomAccessFiles.length; i++) {
                if (randomAccessFiles[i] == null) {
                    randomAccessFiles[i] = new RandomAccessFile(s, "rw");
                    return i;

                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);

        }
        return -1;
    }

    //closes device at given ID
    @Override
    public void Close(int id) {
        if(id >= 0 && id < randomAccessFiles.length && randomAccessFiles[id] != null){
            try{
                randomAccessFiles[id].close();

            } catch (IOException e) {
                throw new RuntimeException(e);

            }

            randomAccessFiles[id] = null;

        }

    }

    //reads the value at the given ID
    //return it in byte[] format
    @Override
    public byte[] Read(int id, int size) {
        byte[] readData = new byte[size];

        try{
            if(randomAccessFiles[id] != null){
                randomAccessFiles[id].write(readData);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        return readData;

    }


    @Override
    public void Seek(int id, int to) {
        try{
            if(randomAccessFiles[id] != null){
                randomAccessFiles[id].seek(to);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public int Write(int id, byte[] data) {
        try{
            if(randomAccessFiles[id] != null){
                randomAccessFiles[id].write(data);
                return data.length;
            }

        } catch (IOException e) {
                throw new RuntimeException(e);

            }

        return  0;

    }
}
