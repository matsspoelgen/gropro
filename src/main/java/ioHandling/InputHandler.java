package ioHandling;

import model.Zugverbindung;

import java.util.ArrayList;

/**
 * Interface zur Generalisierung eines Input-Handlers, der die Eingabe für den Algorithmus vorbereitet.
 * Mit dieser können verschiedene Handler implementiert werden, die
 * beispielsweise die Konsole, eine Datei oder eine Verbindung als Input nutzen.
 *
 * @author Mats Spoelgen
 */
public interface InputHandler {

    /**
     * Gibt die vorbereiteten Daten aus.
     *
     * @return vorbereitete Daten
     */
    public abstract ArrayList<Zugverbindung> getData();
}
