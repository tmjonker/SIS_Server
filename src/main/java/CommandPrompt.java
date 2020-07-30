import java.util.Scanner;

public class CommandPrompt {
     Scanner scanner;
     int input;

    public void launch() {
        scanner = new Scanner(System.in);
        System.out.println("Enter 1 to start server.  Enter 0 to quit.");
        while (input != 0 || input != 1) {
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input == 1) {
                    Thread keepAlive = new Thread(new Paddle(new Server()));
                    keepAlive.start();
                } else if (input == 0) {
                    System.exit(0);
                } else {
                    System.out.println("Enter 1 to start server.  Enter 0 to quit.");
                }
            } else {
                scanner.nextLine();
                System.out.println("Enter 1 to start server.  Enter 0 to quit.");
            }
        }
    }

    public class Paddle implements Runnable {
        Server primaryServer;

        public Paddle(Server server) {
            primaryServer = server;
        }
        @Override
        public void run() {
            while (input != 0) {
                try {
                    primaryServer.acceptConnection();
                } catch (Exception ex) {
                    System.out.println("Paddle." + ex);
                    System.exit(0);
                }
            }
        }
    }
}
