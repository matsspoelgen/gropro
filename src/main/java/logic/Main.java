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
import ioHandling.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;

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
//        inputPath = args[0];
        inputFilePath = "input/file.txt";
//        logger.setConsoleLogging((args.length > 1) && args[1].equals("true"));
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

    private static void outputResult(String result) {
        try {
            if (logger.hasErrors()) {
                System.out.println("Bei der Verarbeitung sind Fehler aufgetreten.");
                output.error(logger.getMessages());
                System.out.printf("Der Log wurde in eine Fehlerdatei geschrieben: %s%n", new File(errorFilePath).getAbsolutePath());
            } else {
                output.write(result);
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
        // Fehler werden hier direkt ausgegeben
        parseFileArgs(args);
        initializeFileIO();

        String result = "";
        try {

            // Algorithmus ausfuehren



            DamenProblem dp = new DamenProblem();
            for (int n : input.getData()) {
                dp.solveProblem(n);
            }



            result = "test";
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            outputResult(result);
        }
    }
}

//TODO keine Umlaute!!