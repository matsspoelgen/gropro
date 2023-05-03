package ioHandling;

/**
 * Abstrakte Klasse zur Generalisierung eines Output-Handlers, der das Ergebnis des Algorithmus weitergibt.
 * Mit dieser koennen verschiedene Handler implementiert werden, die
 * beispielsweise die Konsole, eine Datei oder eine Verbindung als Output nutzen.
 *
 * @author Mats Spoelgen
 */
public abstract class OutputHandler {

    /**
     * Schreibt das Ergebnis des Algorithmus in den Output.
     * @throws Exception
     */
    public abstract void write() throws Exception;
}
