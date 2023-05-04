package ioHandling.file;

import ioHandling.file.exceptions.FileWriteException;
import ioHandling.OutputHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Spezialisierung eines OutputHandlers, die eine Datei als Output nutzt.
 *
 * @author Mats Spoelgen
 */
public class FileWriter extends OutputHandler {

    private final File file;

    /**
     * Erstellt einen neuen FileWriter. Der Pfad der Datei wird uebergeben.
     * @param filePath Pfad der Datei, in die geschrieben wird.
     * @throws FileNotFoundException falls die Ausgabedatei oder das Verzeichnis nicht erstellt werden konnten.
     * @throws FileWriteException falls es zu einem Fehler beim Schreiben in die Datei kommt.
     */
    public FileWriter(String filePath) throws FileNotFoundException, FileWriteException {
        this.file = new File(filePath);

        try {
            new File(this.file.getParent()).mkdirs();
            this.file.createNewFile();
        } catch (Exception e) {
            throw new FileNotFoundException(String.format("Die Ausgabedatei bzw. das Verzeichnis \"%s\" konnten nicht vollstaendig erstellt werden.", this.file.getPath()));
        }

        if (!this.file.isFile()) {
            throw new FileNotFoundException(String.format("Die Ausgabedatei \"%s\" konnte nicht erstellt werden.", this.file.getName()));
        }

        if (!this.file.canWrite()) {
            throw new FileWriteException(String.format("Die Ausgabedatei \"%s\" kann nicht veraendert werden.", this.file.getName()));
        }
    }

    /**
     * Schreibt die Ausgabe des Algorithmus in die Datei.
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     */
    @Override
    public void write() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(this.file);
        // write to file
        pw.close();
    }
}
