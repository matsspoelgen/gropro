package ioHandling.file;

/**
 * Dient zur Bereitstellung von Variablen fuer die Verwaltung von Dateien, die hier zentral Verwaltet werden koennen.
 *
 * @author Mats Spoelgen
 */
public final class ConstantsFileHandling {

    /**
     * Dateiendung fuer Fehlerdateien
     */
    public final static String ERROR_EXTENSION = ".err";

    /**
     * Dateiendung fuer Ausgabedateien
     */
    public final static String OUTPUT_EXTENSION = ".out";

    /**
     * Pfad fuer die Ausgabe von Dateien
     */
    public final static String OUTPUT_PATH = "output/";

    /**
     * Praefix und Erkennungsmerkmal einer Kommentarzeile
     */
    public final static String COMMENT_PREFIX = "%";
}
