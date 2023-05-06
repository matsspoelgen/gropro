package ioHandling;

import java.util.ArrayList;

/**
 * Interface zur Generalisierung eines Output-Handlers, der das Ergebnis des Algorithmus weitergibt.
 * Mit dieser koennen verschiedene Handler implementiert werden, die
 * beispielsweise die Konsole, eine Datei oder eine Verbindung als Output nutzen.
 *
 * @author Mats Spoelgen
 */
public interface OutputHandler {

    /**
     * Schreibt das Ergebnis bei erfolgreicher ausfuehrung des Algorithmus in den Output.
     * @param data Ergebnis des Algorithmus TODO
     * @throws Exception falls ein Fehler beim Ausgeben auftritt
     */
    public abstract void write(String data) throws Exception;

    /**
     * Gibt den Log aus, falls waehrend der Verarbeitung aufgetreten sind.
     * @param messages Log-Eintraege
     * @throws Exception falls ein Fehler beim Ausgeben auftritt
     */
    public abstract void error(ArrayList<String> messages) throws Exception;
}
