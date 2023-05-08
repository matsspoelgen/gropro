package ioHandling.file;

import ioHandling.file.exceptions.FileFormatException;
import ioHandling.file.exceptions.FileReadException;
import ioHandling.InputHandler;
import model.Zugverbindung;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Spezialisierung eines InputHandlers, die eine Datei als Input nutzt.
 *
 * @author Mats Spoelgen
 */
public class FileInput implements InputHandler {

    private final File file;
    private ArrayList<Zugverbindung> verbindungen;

    /**
     * Erstellt einen neuen FileReader. Der Pfad der Eingabedatei wird uebergeben.
     * @param filePath Pfad der Eingabedatei
     * @throws FileNotFoundException falls die Datei nicht gefunden werden kann.
     * @throws FileReadException falls ein Fehler beim Lesen der Datei auftritt.
     * @throws FileFormatException falls die Datei ein ungueltiges Format hat.
     */
    public FileInput(String filePath) throws FileNotFoundException, FileReadException, FileFormatException {
        this.file = new File(filePath);
        this.verbindungen = new ArrayList<>();

        if(!this.file.isFile()) {
            throw new FileNotFoundException(String.format("\"%s\" ist keine Datei oder wurde nicht gefunden.", this.file.getName()));
        }

        if(!this.file.canRead()) {
            throw new FileReadException(String.format("Die Datei \"%s\" kann nicht gelesen werden.", this.file.getName()));
        }

        this.read();
    }

    /**
     * Liest die Eingabedatei ein und speichert die Zugverbindungen ab.
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     * @throws FileFormatException falls die Datei ein ungueltiges Format hat.
     */
    private void read() throws FileNotFoundException, FileFormatException {
        Scanner scanner = new Scanner(this.file);

        if(!scanner.hasNext()) {
            throw new FileFormatException("Die Datei ist leer und damit ungueltig.");
        }

        String line;
        do {
            line = scanner.nextLine();
            if(!line.startsWith(ConstantsFileHandling.COMMENT_PREFIX) && line.matches(ConstantsFileHandling.LINE_VALIDATION_REGEX)) {
                Zugverbindung verbindung = new Zugverbindung(line.split(ConstantsFileHandling.STATION_SEPARATOR));
                if(verbindung.getStationen().size() < 2) {
                    throw new FileFormatException("Eine Verbindung muss mindestens aus zwei unterschiedlichen Stationen bestehen");
                }
                this.verbindungen.add(verbindung);
            }
        } while (scanner.hasNext());

        if(this.verbindungen.size() == 0){
            throw new FileFormatException("Die Datei enthaelt keine gueltigen Verbindungen");
        }

        scanner.close();
    }

    @Override
    public ArrayList<Zugverbindung> getData(){
        return this.verbindungen;
    }
}
