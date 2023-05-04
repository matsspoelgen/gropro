package logic;

import ioHandling.file.ConstantsFileHandling;
import ioHandling.file.FileReader;
import ioHandling.file.FileWriter;
import ioHandling.InputHandler;
import ioHandling.OutputHandler;

import java.io.File;

/**
 * Startklasse des Programms TODO thema
 *
 * @author Mats Spoelgen
 */
public class Main {

    /**
     * Main Methode und Einstiegspunkt des Programms.
     *
     * @param args Startparameter
     *             [0] Name der Eingabedatei
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        if (args != null && args.length == 0) {
//            throw new IllegalArgumentException("Der Dateiname wurde nicht als Parameter uebergeben.");
//        }
//
//        String inputFileName = args[0];
        Logger logger = Logger.getInstance();
        logger.start("main");

        String inputFileName = "../Test/Input/file.txt";
        String outputFileName = ConstantsFileHandling.OUTPUT_PATH
                + new File(inputFileName).getName()
                + ConstantsFileHandling.OUTPUT_EXTENSION;

        InputHandler reader = new FileReader(inputFileName);
        OutputHandler writer = new FileWriter(outputFileName);

        logger.stop("main");
    }

}

//TODO keine Umlaute!!