package logic;

import ioHandling.FileReader;
import ioHandling.FileWriter;
import ioHandling.InputHandler;
import ioHandling.OutputHandler;

/**
 * Keine Umlaute!
 */
public class Main {

    public static void main(String[] args) {
        InputHandler reader = new FileReader("/test");
        OutputHandler writer = new FileWriter("/test");
    }

}