import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // List of clients
    public static ArrayList<CalculatorThread> clientList = new ArrayList<>();

    public static void main(String[] args) {
        // Check to make sure args are all there
        if (args.length < 1) {
            System.err.println("Port is required as a command line argument");
            return;
        }

        // Get port from args
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Port must be an integer");
            return;
        }

        // Initialize logger
        Logger.init();

        // Close logger on shutdown (CTRL+C)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Logger.close();
                System.out.println("Logs saved to log.txt");
            }
        });

        // Run server function & catch all errors
        try {
            runServer(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runServer(int port) throws Exception {
        // Create a thread pool
        ExecutorService pool = Executors.newCachedThreadPool();
        System.out.println("Started Thread Pool for server");

        // Create a server socket
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Started server on port " + port);

            // Accept connections and handle them on a new thread
            while (true) {
                pool.execute(new CalculatorThread(server.accept()));
            }
        }
    }
}