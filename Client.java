import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    public static final String CONNECTION_MESSAGE = "canz i connect :3";
    public static final String CLOSE_MESSAGE = "adios :D";
    public static final String ACK_MESSAGE = "ok";
    public static final String ERR_MESSAGE = "err";

    public static Socket sock = null;
    public static PrintWriter sender = null;

    public static void main(String[] args) {
        // Check to make sure args are all there
        if (args.length < 2) {
            System.err.println("Hostname and port is required as a command line argument");
            return;
        }

        // Get hostname and port
        String hostName = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Port must be an integer");
            return;
        }

        // Generate a random string of characters for the name for the client
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (r.nextInt(26) + 'a'));
        }
        String name = sb.toString();

        // Close socket on shutdown (CTRL+C)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (sender != null) {
                        sender.println(CLOSE_MESSAGE);
                        sender.flush();
                    }
                    if (sock != null) {
                        sock.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            runClient(hostName, port, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void runClient(String hostName, int port, String name) throws Exception {
        // Create the socket and get the streams for sending and receiving
        // data from the server
        sock = new Socket(hostName, port);
        sender = new PrintWriter(sock.getOutputStream());
        BufferedReader receiver = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        // Send initial connection request
        sender.println(CONNECTION_MESSAGE + " " + name);
        sender.flush();

        // Wait for acknowledgement
        String ack = receiver.readLine();
        if (!ack.equals(ACK_MESSAGE)) {
            sock.close();
            throw new ConnectionException("Server did not acknowledge connection request");
        }

        Scanner scan = new Scanner(System.in);

        while (true) {
            // Get calculation request
            System.out.print("Enter calculation: ");
            String request = scan.nextLine();

            // If the request is empty, close the connection
            if (request.equals("")) {
                sender.println(CLOSE_MESSAGE);
                sender.flush();
                break;
            }

            // Send calculation request
            sender.println(request);
            sender.flush();

            // Wait for result
            String result = receiver.readLine();
            System.out.println("Result: " + result);
        }

        scan.close();

        // Close the socket
        sock.close();
    }
}
