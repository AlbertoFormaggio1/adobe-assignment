package util;

public class Utils {
    // simple logger
    public static void log(String msg) {
        String thread = Thread.currentThread().getName();
        String time = java.time.LocalDateTime.now().toString();
        System.out.println(time + " [" + thread + "] " + msg);
    }
}
