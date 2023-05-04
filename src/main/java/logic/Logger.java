package logic;

import java.util.ArrayList;
import java.util.HashMap;

public class Logger {

    private static final Logger instance = new Logger();
    private ArrayList<String> messages = new ArrayList<>();
    private HashMap<String, Long> events = new HashMap<>();



    private Logger() {}

    public static Logger getInstance() {
        return instance;
    }

    public void log(String message) {
        log("info", message);
    }

    public void warn(String message) {
        log("warn", message);
    }

    public void error(String message) {
        log("error", message);
    }

    public void start(String event) {
        if(!this.events.containsKey(event)) {
            this.events.put(event, System.currentTimeMillis());
        }
    }

    public void stop(String event) {
        if(this.events.containsKey(event)) {
            long millis = System.currentTimeMillis() - this.events.get(event);
            log(String.format("Event \"%s\" took %dms (%fs)", event, millis, millis/1000.0f));
        } else {
            warn(String.format("Tried to stop event \"%s\" which was never started.", event));
        }
    }

    private void log(String level, String message) {
        String levelMessage = String.format("%-7s %s", "[" + level + "]", message);
        messages.add(levelMessage);
        System.out.println(levelMessage);
    }
}
