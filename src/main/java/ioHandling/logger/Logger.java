package ioHandling.logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton Logger, der Konsolenausgaben verschiedener Level und Zeitmessung ermoeglicht.
 *
 * @author Mats Spoelgen
 */
public class Logger {

    /**
     * Die Instanzvariable des Singleton-Loggers
     */
    private static final Logger instance = new Logger();

    /**
     * Liste aller geloggten Nachrichten seit Start des Programms
     */
    private ArrayList<String> messages = new ArrayList<>();

    /**
     * Map aller laufenden Events. Schluessel ist Name des Events,
     * Wert ist Startzeitpunkt in Millisekunden
     */
    private HashMap<String, Long> events = new HashMap<>();

    /**
     * Merkt sich, ob waehrend der Verarbeitung Fehler aufgetreten sind.
     */
    private boolean hasErrors = false;

    /**
     * Bestimmt, ob Log-Nachrichten in die Konsole geschrieben werden
     */
    private boolean showConsoleLogging = false;


    /**
     * Privater Konstruktor. Das Erstellen neuer Objekte ist nur der Klasse selbst erlaubt.
     */
    private Logger() {
    }

    /**
     * Ermoeglicht Zugriff auf die einzige Logger-Instanz
     *
     * @return Singleton-Instanz
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Loggt eine Information
     *
     * @param message die Information
     */
    public void log(String message) {
        log(ConstantsLogging.INFO, message);
    }

    /**
     * Loggt eine Warnung
     *
     * @param message die Warnung
     */
    public void warn(String message) {
        log(ConstantsLogging.WARNING, message);
    }

    /**
     * Loggt einen Fehler
     *
     * @param message der Fehler
     */
    public void error(String message) {
        this.hasErrors = true;
        log(ConstantsLogging.ERROR, message);
    }

    /**
     * Loggt Nachrichten aller Level
     *
     * @param level   das gewuenschte Level
     * @param message der gewuenschte Text
     */
    private void log(String level, String message) {
        String levelMessage = String.format("%-7s %s", "[" + level + "]", message);
        messages.add(levelMessage);
        if (this.showConsoleLogging) {
            System.out.println(levelMessage);
        }
    }

    /**
     * Startet die Zeitmessung eines neuen Events
     *
     * @param event Name des Events
     */
    public void start(String event) {
        if (!this.events.containsKey(event)) {
            this.events.put(event, System.currentTimeMillis());
        }
    }

    /**
     * Stoppt die Zeitmessung eines neuen Events.
     * Die vergangene Zeit seit Start der Messung wird ausgegeben.
     *
     * @param event Name des Events
     */
    public void stop(String event) {
        if (this.events.containsKey(event)) {
            long millis = System.currentTimeMillis() - this.events.get(event);
            log(String.format("Event \"%s\" took %dms (%fs)", event, millis, millis / 1000.0f));
        } else {
            warn(String.format("Tried to stop event \"%s\" which was never started.", event));
        }
    }

    /**
     * Gibt aus, ob waehrend der Verarbeitung Fehler aufgetreten sind.
     *
     * @return ob Fehler aufgetreten sind
     */
    public boolean hasErrors() {
        return this.hasErrors;
    }

    /**
     * Gibt alle gesammelten Log-Nachrichten aus
     *
     * @return Log-Nachrichten
     */
    public ArrayList<String> getMessages() {
        return this.messages;
    }

    /**
     * Legt fest, ob Log-Nachrichten auch in die Konsole geschrieben werden sollen
     *
     * @param showConsoleLogging falls in die Konsole geschrieben werden soll
     */
    public void setConsoleLogging(boolean showConsoleLogging) {
        this.showConsoleLogging = showConsoleLogging;
    }
}
