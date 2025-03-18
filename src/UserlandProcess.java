public abstract class UserlandProcess extends Process{

    public static int[][] TLB = new int[2][2];

    static {
        for(int i = 0; i<2; i++){
            TLB[i][0] = -1;
            TLB[i][1] = -1;
        }
    }

    //read and write updated for assignment 6, mainly the getMapping call has been updated to fit with the updated
    //getMapping call in OS
    public byte Read(int addy){
        int vPage = addy / 1024;
        int pOffset = addy % 1024;
        int pPage = getPhysPage(vPage);

        if(pPage == -1){
            OS.getMapping(OS.scheduler.getCurrentlyRunning(), vPage);
            pPage = getPhysPage(vPage);
        }

        if(pPage != -1){
            int pAddy = pPage * 1024 + pOffset;
            return Hardware.memory[pAddy];
        } else {
            throw new IllegalStateException("failed to get physical page.");
        }

    }

    public void Write(int addy, byte value){
        int vPage = addy / 1024;
        int pOffset = addy % 1024;
        int pPage = getPhysPage(vPage);

        if(pPage == -1){
            OS.getMapping(OS.scheduler.getCurrentlyRunning(), vPage);
            pPage = getPhysPage(vPage);
        }

        if(pPage != -1){
            int pAddy = pPage * 1024 + pOffset;
            Hardware.memory[pAddy] = value;
        } else {
            throw new IllegalStateException("fail to get physical page.");
        }
    }

    int getPhysPage(int vPage){
        for (int i = 0; i < 2; i++){
            if(TLB[i][0] == vPage){
                return TLB[i][1];
            }
        }
        return -1;
    }


}
//intentionally left empty to help keep track of which processes are userland and which are kerneland

