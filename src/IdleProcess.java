public class IdleProcess extends UserlandProcess {
    @Override
    protected void main() {
        while (true) {
            cooperate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}
