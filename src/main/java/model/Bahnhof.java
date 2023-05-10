package model;

import java.util.HashSet;

/**
 * Klasse zur Darstellung eines Bahnhofs.
 *
 * @author Mats Spoelgen
 */
public class Bahnhof {

    /**
     * Menge von Indices der Verbindungen
     */
    private final HashSet<Integer> connections;

    /**
     * Name des Bahnhofs
     */
    private final String name;

    /**
     * Konstruktor.
     * @param name Namenskuerzel des Bahnhofs
     */
    public Bahnhof(String name) {
        this.connections = new HashSet<>();
        this.name = name;
    }

    /**
     * Gibt den Namen (Kuerzel) des Bahnhofs aus.
     * @return Namenskuerzel
     */
    public String getName(){
        return this.name;
    }

    /**
     * Fuegt eine neue Verbindung hinzu.
     * @param connectionIndex Index der Verbindung
     */
    public void addConnection(int connectionIndex) {
        this.connections.add(connectionIndex);
    }

    /**
     * Gibt die Verbindungen des Bahnhofs aus.
     * @return Indices der Verbindungen
     */
    public HashSet<Integer> getConnections() {
        return this.connections;
    }

    /**
     * Entfernt eine Menge an Verbindungen des Bahnhofs.
     * @param connections Indices der Verbindungen
     */
    public void removeConnections(HashSet<Integer> connections) {
        this.connections.removeAll(connections);
    }

    // TODO besser
    public void addConnections(HashSet<Integer> connections) {
        this.connections.addAll(connections);
    }

    /**
     * Gibt aus, wie viele Verbindungen durch diesen Bahnhof fuehren.
     * @return Anzahl Verbindungen
     */
    public Integer getConnectionCount() {
        return this.connections.size();
    }

    public String toString() {
        return this.name + ":" + this.connections;
    }
}
