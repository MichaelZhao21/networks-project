import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class for logging messages to a file. This class is a singleton.
 * Logs will also be printed to the console for ease of debugging during
 * development.
 * 
 * Simply call Logger.init() to initialize the logger, Logger.write() to write a
 * message to the log,
 * and Logger.close() to close the log file.
 */
public class Logger {
    private static Logger l;
    private PrintWriter writer;

    /**
     * This is the constructor for the Logger class.
     * This is a singleton class, so it has a private constructor.
     */
    private Logger() {
        try {
            writer = new PrintWriter(new File("log.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will initialize the logger if it has not already been initialized.
     */
    public static void init() {
        if (l == null)
            l = new Logger();
    }

    /**
     * This method will write a message to the log file and console.
     * @param data The message to be written to the log file and console
     */
    public static void log(String data) {
        String currTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String output = "[LOG " + currTime + "] " + data;
        System.out.println(output);
        l.writer.println(output);
        l.writer.flush();
    }

    /**
     * This method will write a message to the log file and console.
     * @param data The message to be written to the log file and console
     * @param clientName The name of the client that sent the request
     */
    public static void log(String data, String clientName) {
        String currTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String output = "[LOG " + currTime + "] " + clientName + " | " + data;
        System.out.println(output);
        l.writer.println(output);
        l.writer.flush();
    }

    /**
     * This method will close the log file writer.
     */
    public static void close() {
        l.writer.close();
    }
}
