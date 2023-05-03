package ioHandling.file.exceptions;

/**
 * Diese Exception wrid geworfen, wenn ein Fehler beim Schreiben in eine Datei auftritt.
 * @author Mats Spoelgen
 */
public class FileWriteException extends Exception {
    public FileWriteException(String message) {
        super(message);
    }
}
