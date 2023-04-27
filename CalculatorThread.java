import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CalculatorThread implements Runnable {
    public Socket client;
    public String clientName;
    public long connectionTime;

    public CalculatorThread(Socket client) {
        this.client = client;
    }

    // This method will keep running while client is connected to server
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

        // Get the current time
        connectionTime = System.currentTimeMillis();

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
                long connTime = System.currentTimeMillis() - connectionTime;
                Logger.log("Client " + clientName + " has disconnected after " + (connTime/1000.0) + "s");
                break;
            }

            // Log the request
            Logger.log("REQUEST: " + request, clientName);

            // Calculate the result
            String result = calculate(request);
            Logger.log("RESULT: " + result, clientName);
            sender.write(result + '\n');
            sender.flush();
        }

        // Close client
        client.close();
    }

    // This method will be called to calculate the result of a calculation request
    public String calculate(String request) throws Exception {
        char[] ops = { '+', '-', '*', '/' };
        int opCount = 0;

        // Fix formatting
        request = request.replace(" ", "");
        for (char op : ops) {
            if (request.indexOf(op) != -1) {
                request = request.replace("" + op, " " + op + " ");
                opCount++;
            }
        }

        // Split input into an array of strings
        String[] input = request.split(" ");

        // Check to make sure the right num of arguments are in there
        if (input.length != 3 || opCount != 1) {
            return "ERROR: Invalid input for calculation. Format: [number] [operation] [number]";
        }

        // Get the operation
        String operation = input[1];

        // Get the numbers
        try {
            double num1 = Double.parseDouble(input[0]);
            double num2 = Double.parseDouble(input[2]);

            // Calculate the result
            double result = 0;
            switch (operation) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num1 / num2;
                    break;
                default:
                    return "ERROR: Invalid operation. Valid operations: +, -, *, /";
            }

            // If possible return an int instead of a double
            if ((result % 1) == 0)
                return Integer.toString((int) result);
            return Double.toString(result);

        } catch (NumberFormatException e) {
            return "ERROR: Invalid numbers in calculation string. Operand must be in the format of a double or int";
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
