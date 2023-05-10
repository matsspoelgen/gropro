package logic;

import ioHandling.file.ConstantsFileHandling;
import ioHandling.file.FileInput;
import ioHandling.file.FileOutput;
import ioHandling.InputHandler;
import ioHandling.OutputHandler;
import ioHandling.file.exceptions.FileCreateException;
import ioHandling.file.exceptions.FileFormatException;
import ioHandling.file.exceptions.FileReadException;
import ioHandling.file.exceptions.FileWriteException;
import ioHandling.logger.ConstantsLogging;
import ioHandling.logger.Logger;
import model.Zugverbindung;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Startklasse des Programms zur Ermittlung minimaler Servicestationen in einem Netz aus Zugverbindugen.
 *
 * @author Mats Spoelgen
 */
public class Main {

    private static final Logger logger = Logger.getInstance();
    private static InputHandler input;
    private static OutputHandler output;
    private static String inputFilePath;
    private static String errorFilePath;

    private static void parseFileArgs(String[] args) {
//        if (args == null || args.length == 0) {
//            throw new IllegalArgumentException("Der Dateiname wurde nicht als Parameter uebergeben.");
//        }
//        inputFilePath = args[0];
        inputFilePath = "input/test1.txt";
        logger.log("Dateiname eingelesen: " + inputFilePath);
        logger.log("Logging parameter eingelesen: true");
        logger.log("Logging in Konsole aktiviert.");
        logger.setConsoleLogging((args.length > 1) && args[1].equals("true"));
        logger.setConsoleLogging(true); // TODO remove
    }

    private static void initializeFileIO() throws FileFormatException, FileNotFoundException, FileReadException, FileWriteException, FileCreateException {
        System.out.printf("Verarbeitung von \"%s\" begonnen...%n", inputFilePath);

        String inputFileName = new File(inputFilePath).getName();
        String outPutFilePath = ConstantsFileHandling.OUTPUT_PATH + inputFileName + ConstantsFileHandling.OUTPUT_EXTENSION;
        errorFilePath = ConstantsFileHandling.OUTPUT_PATH + inputFileName + ConstantsFileHandling.ERROR_EXTENSION;

        logger.log("Erstelle FileInput auf Eingabedatei \"" + inputFilePath + "\"");
        logger.log("Erstelle FileOutput mit Ausgabepfad \"" + outPutFilePath + "\"");
        input = new FileInput(inputFilePath);
        output = new FileOutput(outPutFilePath, errorFilePath);
    }

    private static void outputResult(HashSet<String> serviceStationen) {
        try {
            output.error(logger.getMessages());
            if (logger.hasErrors()) {
                System.out.println("Bei der Verarbeitung sind Fehler aufgetreten.");
                output.error(logger.getMessages());
            } else {
                output.write(serviceStationen);
                System.out.println("Verarbeitung erfolgreich.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Main Methode und Einstiegspunkt des Programms.
     *
     * @param args Startparameter
     *             [0] Name der Eingabedatei
     *             [1] (optional) Boolescher Wert fuer die Darstellung von Log-Eintraegen in der Konsole
     * @throws Exception bei Fehlern in den Startparametern oder der IO-Konfiguration
     */
    public static void main(String[] args) throws Exception {
        logger.start(ConstantsLogging.MAIN);

        // Fehler werden hier direkt ausgegeben
        parseFileArgs(args);
        initializeFileIO();

        HashSet<String> serivceStationen = new HashSet<>();

        try {
            // Algorithmus ausfuehren
            serivceStationen = input.getStreckennetz().getMinStations();

            testResult(serivceStationen, input.getConnections());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            outputResult(serivceStationen);
        }
        logger.stop(ConstantsLogging.MAIN);
    }

    private static void testResult(HashSet<String> servicestationen, ArrayList<Zugverbindung> verbindungen) {
        logger.log("Teste Ergebnis mit urspruenglichen Verbindungen.");
        int unvisited = 0;
        for (Zugverbindung verbindung :
                verbindungen) {
            if (verbindung.getStations().stream().noneMatch(servicestationen::contains)) {
                unvisited++;
            }
        }
        System.out.println("Ergebnis getestet. Unbesuchte Verbindungen: " + unvisited);
    }

}