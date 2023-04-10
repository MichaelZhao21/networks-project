import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CalculatorThread implements Runnable {
    public Socket client;
    public String clientName;

    public CalculatorThread(Socket client) {
        this.client = client;
    }

    public void handleClient() throws Exception {
        // Get the streams for sending and receiving data from the client
        BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter sender = new PrintWriter(client.getOutputStream());

        // Get connection request
        String connRequest = receiver.readLine();
        if (!connRequest.contains(Client.CONNECTION_MESSAGE)) {
            // Close connection and send error
            sender.println(Client.ERR_MESSAGE);
            client.close();
            throw new ConnectionException("Client did not send connection request");
        }

        // Get the client name
        clientName = connRequest.substring(Client.CONNECTION_MESSAGE.length() + 1);

        // Log that the client has connected
        Logger.log("Client " + clientName + " has connected");

        // Add client to list of clients
        Server.clientList.add(this);

        // Send acknowledgement
        sender.println(Client.ACK_MESSAGE);
        sender.flush();

        // Wait for client to send a calculation message
        while (true) {
            String request = receiver.readLine();

            // Exit if client sends close message
            if (request.equals(Client.CLOSE_MESSAGE)) {
                Logger.log("Client " + clientName + " has disconnected");
                break;
            }

            // Log the request
            Logger.log("REQUEST: " + request, clientName);

            // Calculate the result
            String result = calculate(request);
            Logger.log("RESULT: " + result, clientName);
            sender.write(result);
            sender.flush();
        }

        // Close client
        client.close();
    }

    // This method will be called to calculate the result of a calculation request
    public String calculate(String request) throws Exception {
        // Split input into an array of strings
        String[] input = request.split(" ");

        // Check to make sure the right num of arguments are in there
        if (input.length != 3) {
            return "ERROR: Invalid input for calculation. Format: [operation] [number] [number]\n";
        }

        // Get the operation
        String operation = input[0];

        // Get the numbers
        try {
            double num1 = Double.parseDouble(input[1]);
            double num2 = Double.parseDouble(input[2]);

            // Calculate the result
            double result = 0;
            switch (operation) {
                case "ADD":
                    result = num1 + num2;
                    break;
                case "SUB":
                    result = num1 - num2;
                    break;
                case "MUL":
                    result = num1 * num2;
                    break;
                case "DIV":
                    result = num1 / num2;
                    break;
                default:
                    return "ERROR: Invalid operation. Valid operations: ADD, SUB, MUL, DIV\n";
            }

            return result + "\n";
        } catch (NumberFormatException e) {
            return "ERROR: Invalid numbers in calculation string. Numbers must be in the format of a double\n";
        }
    }

    // This is the method that will be called when the thread is started
    @Override
    public void run() {
        // Handle the client
        try {
            handleClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove the client from the list of clients
        Server.clientList.remove(this);
    }
}
