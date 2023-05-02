package ioHandling;

import java.io.FileNotFoundException;

public abstract class OutputHandler {
    public abstract void write() throws FileNotFoundException;
}
