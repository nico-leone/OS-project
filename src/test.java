public class test {
    public static void main(String[] args) {

        //creates the 3 processes in OS as defined below
        OS.CreateProcess(realtime_Test, PCB.Priority.RealTime);

        OS.CreateProcess(interactive_Test, PCB.Priority.Interactive);

        OS.CreateProcess(background_Test, PCB.Priority.Background);

        //starts up OS with the realtime function
        OS.Startup(realtime_Test);


    }

    // static test class for the realtime processes
    static UserlandProcess realtime_Test = new UserlandProcess() {
        //overwrites the run function to make it print as it goes
        //runs for 10 iterations, on the second and fifth iteration it sleeps for
        //3000 ms, and on the 8th iteration it exits
        public void run() {

            for (int i = 0; i < 10; i++) {

                System.out.println("ProcessA(RealTime): running");

                if (i == 2 || i == 6) {

                    System.out.println("ProcessA: sleeping for 3000ms");

                    OS.Sleep(3000);
                }


                if (i == 8) {

                    System.out.println("ProcessA: Exiting");

                    OS.Exit();
                    break;
                }
            }
        }
        //main function
        @Override
        protected void main() {

        }

        @Override
        public void stop() {}
    };

    //similar to the realtime test, but sleeps at different intervals and exits at the 9th
    static UserlandProcess interactive_Test = new UserlandProcess() {
        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {

                System.out.println("ProcessB(Interactive): running");

                if (i == 3 || i == 6) {

                    System.out.println("ProcessB: sleeping for 3000ms");

                    OS.Sleep(4000);
                }


                if (i == 8) {

                    System.out.println("ProcessB: Exiting");

                    OS.Exit();
                    break;
                }
            }
        }

        @Override
        protected void main() {

        }

        @Override
        public void stop() {}
    };

    //background test runs 5 times and sleeps for 10 seconds after each run, then exits
    static UserlandProcess background_Test = new UserlandProcess() {
        @Override
        public void run() {

            for (int i = 0; i < 5; i++) {

                System.out.println("ProcessC(Background): running");
                OS.Sleep(10);
            }


            System.out.println("ProcessC: exiting");
            OS.Exit();
        }

        @Override
        protected void main() {

        }

        @Override
        public void stop() {
        }
    };
}

