package ioHandling.file;

import ioHandling.file.exceptions.FileFormatException;
import ioHandling.file.exceptions.FileReadException;
import ioHandling.InputHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spezialisierung eines InputHandlers, die eine Datei als Input nutzt.
 *
 * @author Mats Spoelgen
 */
public class FileInput implements InputHandler {

    private final File file;
    private final ArrayList<Integer> data;

    /**
     * Erstellt einen neuen FileReader. Der Pfad der Eingabedatei wird uebergeben.
     * @param filePath Pfad der Eingabedatei
     * @throws FileNotFoundException falls die Datei nicht gefunden werden kann.
     * @throws FileReadException falls ein Fehler beim Lesen der Datei auftritt.
     * @throws FileFormatException falls die Datei ein ungueltiges Format hat.
     */
    public FileInput(String filePath) throws FileNotFoundException, FileReadException, FileFormatException {
        this.file = new File(filePath);
        this.data = new ArrayList<>();

        if(!this.file.isFile()) {
            throw new FileNotFoundException(String.format("\"%s\" ist keine Datei oder wurde nicht gefunden.", this.file.getName()));
        }

        if(!this.file.canRead()) {
            throw new FileReadException(String.format("Die Datei \"%s\" kann nicht gelesen werden.", this.file.getName()));
        }

        this.read();
    }

    /**
     * Liest die Eingabedaten aus der Datei und bereitet diese fuer den Algorithmus vor.
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     * @throws FileFormatException falls die Datei ein ungueltiges Format hat.
     */
    private void read() throws FileNotFoundException, FileFormatException {
        Scanner scanner = new Scanner(this.file);

        if(!scanner.hasNext()) {
            throw new FileFormatException("Die Datei ist leer und damit ungueltig.");
        }

        ArrayList<String> inputLines = new ArrayList<>();
        String line;

        do {
            line = scanner.nextLine();
            if(line.startsWith(ConstantsFileHandling.COMMENT_PREFIX)) {
                // line is comment
            } else {
                // standard
            }
        } while (scanner.hasNext());

        this.data.add(5);

        scanner.close();
    }

    @Override
    public ArrayList<Integer> getData(){
        return this.data;
    }
}
