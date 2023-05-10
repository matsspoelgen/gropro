package logic;

import ioHandling.logger.ConstantsLogging;
import ioHandling.logger.Logger;
import model.Bahnhof;
import model.Zugverbindung;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Streckennetz {

    private final ArrayList<Zugverbindung> connections;
    private final HashMap<String, Bahnhof> stations;
    private final Logger logger;

    public Streckennetz(ArrayList<Zugverbindung> connections, HashMap<String, Bahnhof> stations) {
        this.logger = Logger.getInstance();
        this.connections = connections;
        this.stations = stations;
    }

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

    private ArrayList<Bahnhof> copyAndRemoveStations(ArrayList<Bahnhof> stations, HashSet<Integer> removedConnections) {
        ArrayList<Bahnhof> ret = new ArrayList<>();

        for (int i = stations.size() - 1; i >= 0; i--) {
            Bahnhof old = stations.get(i);
            Bahnhof newStation = new Bahnhof(old.getName());
            newStation.addConnections(old.getConnections());
            newStation.removeConnections(removedConnections);
            ret.add(newStation);
        }
        ret.sort(Comparator.comparingInt(a -> -a.getConnectionCount()));
        return ret;
    }

    private void getMinStationsRek(HashSet<String> current, HashSet<String> shortest, ArrayList<Bahnhof> remainingStations, HashSet<Integer> remainingConnections) {

        if (remainingConnections.isEmpty()) { // Loesung gefunden
            shortest.clear();
            shortest.addAll(current);
            logger.log("Zwischenergebnis: " + shortest);
            return;
        }

        for (Bahnhof station : remainingStations) {
            String currentName = station.getName();

            if (current.contains(station.getName())) {
                continue;
            }

            // reichen die maximal verbleibenden Stationen nicht aus, um die uebrigen Verbindungen abzudecken,
            // kann hier frueher beendet werden.
            // Das kann mit der Verbindungszahl der aktuellen Station abgeschaetzt werden, da alle folgenden Stationen
            // garantiert werniger Verbindungen haben.

            int stepsTillSkip = shortest.size() - current.size();
            if(shortest.size() != 0 && remainingConnections.size() >= stepsTillSkip * station.getConnectionCount()) {
                return;
            }

            HashSet<Integer> removedConnections = new HashSet<>(this.stations.get(currentName).getConnections());
            HashSet<Integer> originalConnections = new HashSet<>(remainingConnections);
            remainingConnections.removeAll(removedConnections);                                 // entferne alle neu bekannten Verbindungen

            ArrayList<Bahnhof> originalStations = new ArrayList<>(remainingStations);
            remainingStations = copyAndRemoveStations(remainingStations, removedConnections);   // entferne die Verbindungen aus den Stationen und sortiere diese neu

            current.add(currentName);
            getMinStationsRek(current, shortest, remainingStations, remainingConnections);      // naechste Station
            remainingStations = originalStations;                                               // originalwerte wiederherstellen
            remainingConnections = originalConnections;
            current.remove(currentName);

        }
    }

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
