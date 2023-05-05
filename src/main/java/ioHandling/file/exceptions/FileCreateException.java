package ioHandling.file.exceptions;

/**
 * Diese Exception wird geworfen, falls ein Fehler bei der Erstellung einer Datei, oder eines Verzeichnisses auftritt.
 *
 * @author Mats Spoelgen
 */
public class FileCreateException extends Exception {

    /**
     * Konstruktor
     *
     * @param message Nachricht
     */
    public FileCreateException(String message) {
        super(message);
    }
}
