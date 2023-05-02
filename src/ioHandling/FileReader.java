package ioHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader extends InputHandler {

    private final String filePath;
    private String data;

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void read() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(this.filePath));
        // process file content
        // set data
    }

    @Override
    public String getData(){
        return this.data;
    }
}
