import java.io.File;
import java.io.PrintWriter;

/**
 * Logger class for logging messages to a file. This class is a singleton.
 * Logs will also be printed to the console for ease of debugging during development.
 * 
 * Simply call Logger.init() to initialize the logger, Logger.write() to write a message to the log,
 * and Logger.close() to close the log file.
 */
public class Logger {
    private static Logger l;
    private PrintWriter writer;

    private Logger() {
        try {
            writer = new PrintWriter(new File("log.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (l == null)
        l = new Logger();
    }

    public static void log(String data) {
        System.out.println("[LOG] " + data);
        l.writer.println(data);
        l.writer.flush();
    }

    public static void log(String data, String clientName) {
        String output = "[LOG] " + clientName + " | " + data;
        System.out.println(output);
        l.writer.println(output);
        l.writer.flush();
    }

    public static void close() {
        l.writer.close();
    }
}
