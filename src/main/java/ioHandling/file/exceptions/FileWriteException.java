package ioHandling.file.exceptions;

/**
 * Diese Exception wrid geworfen, wenn ein Fehler beim Schreiben in eine Datei auftritt.
 * @author Mats Spoelgen
 */
public class FileWriteException extends Exception {

    /**
     * Konstruktor
     *
     * @param message Nachricht
     */
    public FileWriteException(String message) {
        super(message);
    }
}
