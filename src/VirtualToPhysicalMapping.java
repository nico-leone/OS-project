//simple class for assignment in lab 6
public class VirtualToPhysicalMapping {
    public int physicalPageNumber;
    public int onDiskPageNumber;

    public VirtualToPhysicalMapping() {
        this.physicalPageNumber = -1;
        this.onDiskPageNumber = -1;
    }
}
