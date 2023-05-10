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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

/**
 * Startklasse des Programms TODO thema
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
        inputFilePath = "input/random_60_20.txt";
        logger.setConsoleLogging((args.length > 1) && args[1].equals("true"));
        logger.setConsoleLogging(true);
    }

    private static void initializeFileIO() throws FileFormatException, FileNotFoundException, FileReadException, FileWriteException, FileCreateException {
        System.out.printf("Verarbeitung von \"%s\" begonnen...%n", inputFilePath);

        String inputFileName = new File(inputFilePath).getName();
        String outPutFilePath = ConstantsFileHandling.OUTPUT_PATH + inputFileName + ConstantsFileHandling.OUTPUT_EXTENSION;
        errorFilePath = ConstantsFileHandling.OUTPUT_PATH + inputFileName + ConstantsFileHandling.ERROR_EXTENSION;
        input = new FileInput(inputFilePath);
        output = new FileOutput(outPutFilePath, errorFilePath);
    }

    private static void outputResult(HashSet<String> serviceStationen) {
        try {
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

        // Algorithmus ausfuehren
//        serivceStationen = new Streckennetz(input.getData()).getMinStations();
        serivceStationen = new Streckennetz(input.getData()).getMinStationsTS();

        try {

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            outputResult(serivceStationen);
        }
        logger.stop(ConstantsLogging.MAIN);
    }
}

//TODO keine Umlaute!!