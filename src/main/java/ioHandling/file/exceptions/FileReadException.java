package ioHandling.file.exceptions;

/**
 * Diese Exception wird geworfen, falls ein Fehler beim Lesen einer Datei auftritt.
 *
 * @author Mats Spoelgen
 */
public class FileReadException extends Exception {

    /**
     * Konstruktor
     *
     * @param message Nachricht
     */
    public FileReadException(String message) {
        super(message);
    }
}
