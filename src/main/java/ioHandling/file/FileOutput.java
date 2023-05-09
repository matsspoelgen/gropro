package ioHandling.file;

import ioHandling.file.exceptions.FileCreateException;
import ioHandling.file.exceptions.FileWriteException;
import ioHandling.OutputHandler;
import ioHandling.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Spezialisierung eines OutputHandlers, die eine Datei als Output nutzt.
 *
 * @author Mats Spoelgen
 */
public class FileOutput implements OutputHandler {

    private final File file;
    private final String errorFilePath;
    private final Logger logger;

    /**
     * Erstellt einen neuen FileWriter. Der Pfad der Datei wird uebergeben.
     *
     * @param filePath      Pfad der Datei, in die das Ergebnis des Algorithmus geschrieben wird
     * @param errorFilePath Pfad der Datei, die im Fall eines Fehlers erstellt wird
     * @throws FileWriteException  falls es zu einem Fehler beim Schreiben in die Datei kommt
     * @throws FileCreateException falls es zu einem Fehler beim Erstellen der Ausgabedatei gibt
     */
    public FileOutput(String filePath, String errorFilePath) throws FileWriteException, FileCreateException {
        this.logger = Logger.getInstance();
        this.file = getFile(filePath);
        this.errorFilePath = errorFilePath;
    }

    /**
     * Gibt eine Datei am gewuenschten Pfad zurueck.
     * Falls Datei, oder Pfad nicht existieren, werden diese erstellt.
     *
     * @param filePath Pfad der Datei
     * @return Die (erstellte) Datei
     * @throws FileCreateException falls die Datei bzw. das Verzeichnis nicht erstellt werden konnten.
     * @throws FileWriteException  falls in die Datei nicht geschrieben werden kann.
     */
    private File getFile(String filePath) throws FileCreateException, FileWriteException {
        File file = new File(filePath);
        try {
            if (file.getParentFile().mkdirs()) {
                logger.log(String.format("Verzeichnis \"%s\" erstellt", file.getParent()));
            }
            if (file.createNewFile()) {
                logger.log(String.format("Datei \"%s\" erstellt", file.getPath()));
            }
        } catch (Exception e) {
            throw new FileCreateException(String.format("Die Datei bzw. das Verzeichnis \"%s\" konnten nicht vollstaendig erstellt werden.", file.getPath()));
        }

        if (!file.isFile()) {
            throw new FileCreateException(String.format("Die Datei \"%s\" konnte nicht erstellt werden.", file.getName()));
        }

        if (!file.canWrite()) {
            throw new FileWriteException(String.format("Die Datei \"%s\" kann nicht veraendert werden.", file.getName()));
        }
        return file;
    }

    /**
     * Schreibt die Position der Servicestationen in die Ausgabedatei.
     *
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     */
    @Override
    public void write(HashSet<String> serviceStationen) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(this.file);
        Iterator<String> stationen = serviceStationen.iterator();
        while (stationen.hasNext()) {
            pw.print(stationen.next());
            if (stationen.hasNext()) {
                pw.print(ConstantsFileHandling.STATION_SEPARATOR);
            }
        }
        pw.close();
        System.out.printf("Die Loesung wurde in die Ausgabedatei geschrieben: %s%n", this.file.getAbsolutePath());
    }

    /**
     * Schreibt aufgetretene Fehler in eine Fehlerdatei.
     *
     * @param messages Log-Eintraege
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     * @throws FileWriteException    falls in die Fehlerdatei nicht geschrieben werden kann
     */
    @Override
    public void error(ArrayList<String> messages) throws FileCreateException, FileNotFoundException, FileWriteException {
        File errorFile = getFile(this.errorFilePath);
        PrintWriter pw = new PrintWriter(errorFile);
        for (String msg : messages) {
            pw.println(msg);
        }
        pw.close();
        System.out.printf("Der Log wurde in eine Fehlerdatei geschrieben: %s%n", errorFile.getAbsolutePath());
    }
}
