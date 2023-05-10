package ioHandling.file;

import ioHandling.file.exceptions.FileFormatException;
import ioHandling.file.exceptions.FileReadException;
import ioHandling.InputHandler;
import ioHandling.logger.Logger;
import logic.Streckennetz;
import model.Bahnhof;
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
    private final ArrayList<Zugverbindung> connections;
    private final HashMap<String, Bahnhof> stations;
    private final Logger logger;

    /**
     * Erstellt einen neuen FileReader. Der Pfad der Eingabedatei wird uebergeben.
     *
     * @param filePath Pfad der Eingabedatei
     * @throws FileNotFoundException falls die Datei nicht gefunden werden kann.
     * @throws FileReadException     falls ein Fehler beim Lesen der Datei auftritt.
     * @throws FileFormatException   falls die Datei ein ungueltiges Format hat.
     */
    public FileInput(String filePath) throws FileNotFoundException, FileReadException, FileFormatException {
        this.logger = Logger.getInstance();
        this.file = new File(filePath);
        this.connections = new ArrayList<>();
        this.stations = new HashMap<>();

        if (!this.file.isFile()) {
            throw new FileNotFoundException(String.format("\"%s\" ist keine Datei oder wurde nicht gefunden.", this.file.getName()));
        }

        if (!this.file.canRead()) {
            throw new FileReadException(String.format("Die Datei \"%s\" kann nicht gelesen werden.", this.file.getName()));
        }

        this.read();
    }

    /**
     * Liest die Eingabedatei ein und speichert die Zugverbindungen und Bahnhoefe ab.
     *
     * @throws FileNotFoundException falls ein Fehler beim Zugriff auf die Datei auftritt.
     * @throws FileFormatException   falls die Datei ein ungueltiges Format hat.
     */
    private void read() throws FileNotFoundException, FileFormatException {
        Scanner scanner = new Scanner(this.file);
        logger.log("Einlesevorgang gestartet");

        if (!scanner.hasNext()) {
            throw new FileFormatException("Die Datei ist leer und damit ungueltig.");
        }

        String line;
        do {
            line = scanner.nextLine();
            if (!line.startsWith(ConstantsFileHandling.COMMENT_PREFIX) && line.matches(ConstantsFileHandling.LINE_VALIDATION_REGEX)) {
                Zugverbindung connection = new Zugverbindung(line.split(ConstantsFileHandling.STATION_SEPARATOR));
                if (connection.getStations().size() < 2) {
                    throw new FileFormatException("Eine Verbindung muss mindestens aus zwei unterschiedlichen Stationen bestehen"); //TODO kein error...
                }
                this.connections.add(connection);
            }
        } while (scanner.hasNext());

        scanner.close();

        if (this.connections.size() == 0) {
            throw new FileFormatException("Die Datei enthaelt keine gueltigen Verbindungen");
        }

        logger.log("Erstelle Bahnhoefe aus Zugverbindungen");

        for (Zugverbindung connection : this.connections) {
            for (String station : connection.getStations()) {
                if (!stations.containsKey(station)) {
                    stations.put(station, new Bahnhof(station));
                }
            }
        }

        System.out.printf("Einlesevorgang abgeschlossen, %d Zugverbindungen und %d Bahnhoefe eingelesen%n", this.connections.size(), this.stations.size());
    }

    /**
     * Gibt das Streckennetz aus, das aus der Eingabedatei erstellt wurde.
     *
     * @return Streckennetz
     */
    @Override
    public Streckennetz getStreckennetz() {
        logger.log("Erstelle neues Streckennetz");
        return new Streckennetz(new ArrayList<>(this.connections), new HashMap<>(this.stations));
    }

    /**
     * Gibt die Verbindungen des urspruenglichen Streckennetzes aus.
     * Wird fuer das Testen des Ergebnisses verwendet.
     *
     * @return Verbindungen des Streckennetzes
     */
    @Override
    public ArrayList<Zugverbindung> getConnections() {
        return new ArrayList<>(this.connections);
    }
}
