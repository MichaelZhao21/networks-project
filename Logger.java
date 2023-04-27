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
        String currTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis()));
        System.out.println("[LOG " + currTime + "] " + data);
        l.writer.println(data);
        l.writer.flush();
    }

    public static void log(String data, String clientName) {
        String currTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String output = "[LOG " + currTime + "] " + clientName + " | " + data;
        System.out.println(output);
        l.writer.println(output);
        l.writer.flush();
    }

    public static void close() {
        l.writer.close();
    }
}
