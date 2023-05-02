package ioHandling;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter extends OutputHandler {

    private final String fileName;

    public FileWriter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void write() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(this.fileName);
    }
}
