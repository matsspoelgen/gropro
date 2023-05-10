package logic;

import ioHandling.logger.ConstantsLogging;
import ioHandling.logger.Logger;
import model.Bahnhof;
import model.Zugverbindung;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Klasse zur Berechnung minimaler Servicestationen.
 *
 * @author Mats Spoelgen
 */
public class Streckennetz {

    private final ArrayList<Zugverbindung> connections;
    private final HashMap<String, Bahnhof> stations;
    private final Logger logger;


    /**
     * Konstruktor
     *
     * @param connections Zugverbindungen mit Bahnhofskuerzeln
     * @param stations    Bahnhoefe mit (leeren) Verbindungs-Indices
     */
    public Streckennetz(ArrayList<Zugverbindung> connections, HashMap<String, Bahnhof> stations) {
        this.logger = Logger.getInstance();
        this.connections = connections;
        this.stations = stations;
    }

    /**
     * Reduziert Verbindungen, verknuepft Bahnhoefe mit reduzierten
     * Verbindungen und reduziert anschliessend die Bahnhoafe
     */
    public void reduce() {
        reduceConnections();

        // ordne allen Stationen die reduzierten Zugverbindungen zu
        for (int i = 0; i < this.connections.size(); i++) {
            for (String station : this.connections.get(i).getStations()) {
                this.stations.get(station).addConnection(i);
            }
        }

        reduceStations();
    }

    /**
     * Eintrittspunkt fuer rekursive Ermittlung einer minimalen Menge an ServiceStationen.
     *
     * @return minimale Servicestationen
     */
    public HashSet<String> getMinStations() {
        HashSet<String> serviceStations = new HashSet<>();

        logger.start(ConstantsLogging.MIN_STATIONS);

        reduce();

        ArrayList<Bahnhof> remainingStations = new ArrayList<>(this.stations.values());
        HashSet<Integer> remainingConnections = new HashSet<>(IntStream.range(0, this.connections.size()).boxed().toList()); // Indices der Verbindungen in this.connections

        remainingStations.sort(Comparator.comparingInt(a -> -a.getConnectionCount()));

        System.out.printf("Starte Algorithmus mit %d Verbindungen und %d Stationen%n", this.connections.size(), this.stations.size());

        getMinStationsRek(new HashSet<>(), serviceStations, remainingStations, remainingConnections);

        logger.stop(ConstantsLogging.MIN_STATIONS);
        System.out.printf("Ergebnis ermittelt: %d Stationen in %s%n", serviceStations.size(), serviceStations);

        return serviceStations;
    }

    /**
     * Ermittelt rekursiv die minimalen Servicestationen mit Backtracking.
     *
     * @param current              aktuelle Menge an Servicestationen
     * @param shortest             bisher bestes Ergebnis, Menge an Servicestationen, die alle Verbindungen abdecken
     * @param sortedStations       Verbleibende Bahnhoefe, die die unbekannten Verbindungen erreichen
     * @param remainingConnections Verbleibende Verbindungen, die von den aktuellen Servicestationen nicht erreicht werden koennen
     */
    private void getMinStationsRek(HashSet<String> current, HashSet<String> shortest, ArrayList<Bahnhof> sortedStations, HashSet<Integer> remainingConnections) {

        if (remainingConnections.isEmpty()) { // Loesung gefunden
            shortest.clear();
            shortest.addAll(current);
            logger.log("Zwischenergebnis: " + shortest);
            return;
        }

        for (Bahnhof station : sortedStations) {
            String currentName = station.getName();

            if (current.contains(station.getName())) {
                continue;
            }

            // reichen die maximal verbleibenden Stationen nicht aus, um die uebrigen Verbindungen abzudecken,
            // kann hier frueher beendet werden.
            // Das kann mit der Verbindungszahl der aktuellen Station abgeschaetzt werden, da alle folgenden Stationen
            // garantiert werniger Verbindungen haben.

            int stepsTillSkip = shortest.size() - current.size();
            if (shortest.size() != 0 && remainingConnections.size() >= stepsTillSkip * station.getConnectionCount()) {
                return;
            }

            HashSet<Integer> originalConnections = new HashSet<>(remainingConnections);
            HashSet<Integer> removedConnections = new HashSet<>(this.stations.get(currentName).getConnections());
            remainingConnections.removeAll(removedConnections);

            ArrayList<HashSet<Integer>> removedConnectionsPerStation = new ArrayList<>(sortedStations.size());

            for (int i = 0; i < sortedStations.size(); i++) {                                                           // Verbindungen auch aus den Stationen entfernen
                for (int cIndex : removedConnections) {
                    if (!sortedStations.get(i).getConnections().contains(cIndex)) {
                        removedConnectionsPerStation.add(i, null);
                    }
                    removedConnectionsPerStation.add(i, new HashSet<>());
                    removedConnectionsPerStation.get(i).add(cIndex);                                                    // entfernte Verbindungen pro Station in Liste abspeichern
                }
                sortedStations.get(i).removeConnections(removedConnections);
            }

            ArrayList<Bahnhof> nextOrder = new ArrayList<>(sortedStations);
            nextOrder.sort(Comparator.comparingInt(a -> -a.getConnectionCount()));                                      // neue Sortierung fuer naechste Station

            current.add(currentName);
            getMinStationsRek(current, shortest, nextOrder, remainingConnections);                                           // naechste Station
            current.remove(currentName);                                                                                // originalwerte wiederherstellen
            remainingConnections = originalConnections;

            for (int i = 0; i < sortedStations.size(); i++) {                                                           // fuege entfernte Verbindungen wieder zu den Bahnhoefen hinzu
                if (removedConnectionsPerStation.get(i) == null) {
                    continue;
                }
                sortedStations.get(i).addConnections(removedConnectionsPerStation.get(i));
            }

        }
    }

    /**
     * Algorithmus nach Prinzip der Datenreduktionstechnik 2.
     * Vergleich die Verbindungen aller Bahnhoefe. Deckt ein Bahnhof die Verbindungen
     * eines anderen Bahnhofs ab, kann letzterer entfernt werden.
     */
    private void reduceStations() {
        HashSet<String> reducedStations = new HashSet<>();

        logger.start(ConstantsLogging.REDUCE_STATIONS);

        for (String keyA : this.stations.keySet()) {
            for (String keyB : this.stations.keySet()) {
                if (keyA.equals(keyB) || reducedStations.contains(keyA) || reducedStations.contains(keyB)) {
                    continue;
                }

                HashSet<Integer> connectionsA = this.stations.get(keyA).getConnections();
                HashSet<Integer> connectionsB = this.stations.get(keyB).getConnections();

                if (connectionsB.containsAll(connectionsA)) {
                    reducedStations.add(keyA);
                    for (Zugverbindung connection : this.connections) {
                        connection.removeStation(keyA);
                    }
                }
            }
        }

        for (String station : reducedStations) {
            this.stations.remove(station);
        }
        logger.log(String.format("Bahnhoefe entfernt: (%d) %s", reducedStations.size(), reducedStations.size() > 0 ? reducedStations : ""));
        logger.stop(ConstantsLogging.REDUCE_STATIONS);
    }

    /**
     * Algorithmus nach Prinzip der Datenreduktionstechnik 3.
     * Vergleicht die Bahnhoefe aller Verbindungen. Haelt eine Verbindunge an allen Bahnhoefen,
     * an denen auch schon eine weitere Verbindung haelt, kann sie entfernt werden.
     */
    protected void reduceConnections() {
        List<Zugverbindung> reducedConnections = new ArrayList<>();
        logger.start(ConstantsLogging.REDUCE_CONNECTIONS);

        for (Zugverbindung v1 : connections) {
            for (Zugverbindung v2 : connections) {
                if (v1 == v2 || reducedConnections.contains(v1)) continue;

                if (v1.getStations().containsAll(v2.getStations())) {
                    reducedConnections.add(v1.getStations().size() > v2.getStations().size() ? v1 : v2);
                }
            }
        }

        connections.removeAll(reducedConnections);
        logger.log(String.format("Zugverbindungen entfernt: (%d) %s", reducedConnections.size(), reducedConnections.size() > 0 ? reducedConnections : ""));
        logger.stop(ConstantsLogging.REDUCE_CONNECTIONS);

    }

}
