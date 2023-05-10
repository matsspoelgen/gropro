package ioHandling;

import logic.Streckennetz;
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
     * Gibt das Streckennetz aus.
     *
     * @return Streckennetz
     */
    Streckennetz getStreckennetz();

    /**
     * Gibt die Verbindungen des urspruenglichen Streckennetzes aus.
     * Wird fuer das Testen des Ergebnisses verwendet.
     *
     * @return Verbindungen des Streckennetzes
     */
    ArrayList<Zugverbindung> getConnections();
}
