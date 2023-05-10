package model;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Klasse zur Darstellung einer Zugverbindung.
 *
 * @author Mats Spoelgen
 */
public class Zugverbindung {

    /**
     * Menge der Namenskuerzel der Bahnhoefe auf der Verbindung
     */
    private final HashSet<String> stationen;

    /**
     * Konstruktor
     * @param stationen Kuerzel der Bahnhoefe
     */
    public Zugverbindung(String[] stationen) {
        this.stationen = new HashSet<>(Arrays.asList(stationen));
    }

    /**
     * Gibt die Bahnhoefe aus
     * @return  Kuerzel der Bahnhoefe
     */
    public HashSet<String> getStations() {
        return this.stationen;
    }

    /**
     * Entfernt einen Bahnhof aus der Verbindung
     * @param station zu entfernen
     */
    public void removeStation(String station) {
        this.stationen.remove(station);
    }

    public String toString() {
        return stationen.toString();
    }
}
