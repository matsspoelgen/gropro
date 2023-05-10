package ioHandling.logger;

/**
 * Dient zur Bereitstellung von Variablen fuer Logging, die hier zentral Verwaltet werden koennen.
 *
 * @author Mats Spoelgen
 */
public final class ConstantsLogging {

    /**
     * String fuer Infos im Log
     */
    public static final String INFO = "info";

    /**
     * String fuer Warnungen im Log
     */
    public static final String WARNING = "warn";

    /**
     * String fuer Fehler im Log
     */
    public static final String ERROR = "error";

    /**
     * Log Event fuer das gesamte Programm
     */
    public static final String MAIN = "main";

    /**
     * Log Event fuer den Hauptalgorithmus
     */
    public static final String MIN_STATIONS = "min_stations";

    /**
     * Log Event fuer Datenreduktoinstechnik 2
     */
    public static final String REDUCE_STATIONS = "reduce_stations";

    /**
     * Log Event fuer Datenreduktionstechnik 3
     */
    public static final String REDUCE_CONNECTIONS = "reduce_connections";
}
