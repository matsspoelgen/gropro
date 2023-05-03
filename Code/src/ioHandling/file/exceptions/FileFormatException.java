package ioHandling.file.exceptions;

/**
 * Diese Exception wird geworfen, falls die Eingabedatei nicht den vorgegebenen Restriktionen entspricht.
 * @author Mats Spoelgen
 */
public class FileFormatException extends Exception{
    public FileFormatException(String message) {
        super(message);
    }
}
